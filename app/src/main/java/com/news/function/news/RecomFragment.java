package com.news.function.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import com.news.R;
import com.news.base.BaseAdapter;
import com.news.base.BaseFrament;
import com.news.base.BaseViewHolder;
import com.news.constant.Local;
import com.news.dao.DbUtils;
import com.news.utils.T;
import com.news.entity.NewsEntity;
import com.news.function.news.common.CommonViewHolder;
import com.news.function.news.common.NewsDetailActivity;
import com.news.listener.OnItemClickListener;
import com.news.network.NetWork;
import com.news.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/10/13.
 */
public class RecomFragment extends BaseFrament {
    private static final String TAG = "RecomFragment";
    private static int NEWS_TYPE = 0;
    private DbManager db = DbUtils.init();
    public static int limit = 10;
    private static int page = 1;
    private TechnoAdapter adapter;
    private List<NewsEntity> list;
    private Boolean isRefresh = true;
    private Boolean isLoadMore = false;
    private int totalItemCount;
    private int lastVisibleItemPosition;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.frament_news_layout;
    }

    @Override
    protected void initView() {
        page = 1;
        adapter = new TechnoAdapter();
        adapter.setOnClickListener(new OnItemClickListener<NewsEntity>() {
            @Override
            public void onItemClick(NewsEntity itemValue, int viewID, int position) {
                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("DATA",itemValue);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        list = new ArrayList<NewsEntity>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
        mRecyclerView.setAdapter(adapter);
        refresh.setColorSchemeResources(android.R.color.holo_purple, android.R.color.holo_blue_bright, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!NetworkUtils.isNetworkAvailable(getActivity())) {
                    T.Shows(getActivity(), "网络无法连接.");
                    refresh.setRefreshing(false);
                    return;
                }

                page++;
                isRefresh = true;
                getNews();
            }
        });

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (linearLayoutManager.getItemCount() == 0) {
                    return;
                }
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoadMore && totalItemCount <= (lastVisibleItemPosition + 1)) {
                    //此时是刷新状态
                    page++;
                    isLoadMore = true;
                    getNews();
                }
            }
        });
    }

    @Override
    protected void initData() {
        getNews();
    }

    public void getNews() {
        if(NetworkUtils.isNetworkAvailable(getActivity())) {
            NetWork.getNetWorkApi2()
                    .getRecom(Local.SPORT_API_KEY, limit, page)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }else {
            try {
                isLoadMore = false;
                List<NewsEntity> lists = db.selector(NewsEntity.class).where("type", "=", NEWS_TYPE).limit(limit).offset((page-1)*limit).orderBy("id").findAll();
                if(lists==null||lists.size()==0){
                    T.Shows(getActivity(), "暂无更多数据.");
                    adapter.notifyItemRemoved(adapter.getItemCount());
                    return;
                }
                list.addAll(lists);
                adapter.refreshData(list);
                refresh.setRefreshing(false);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    Observer<String> observer = new Observer<String>() {
        @Override
        public void onCompleted() {
            refresh.setRefreshing(false);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            refresh.setRefreshing(false);
        }

        @Override
        public void onNext(String s) {
            isLoadMore = false;
            refresh.setRefreshing(false);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(s);
                String code = jsonObject.optString("code", "");
                String msg = jsonObject.optString("msg", "");
                if (code.equals("200") && msg.equals("success")) {
                    if (isRefresh) {
                        list.clear();
                        isRefresh = false;
                        db.delete(NewsEntity.class, WhereBuilder.b("type", "=", NEWS_TYPE));
                    }
                    JSONArray jsonArray = jsonObject.getJSONArray("newslist");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        NewsEntity item = new NewsEntity();
                        item.setCtime(obj.getString("ctime"));
                        item.setTitle(obj.getString("title"));
                        item.setDescription(obj.getString("description"));
                        item.setPicUrl(obj.getString("picUrl"));
                        item.setUrl(obj.getString("url"));
                        item.setType(NEWS_TYPE);
                        list.add(item);
                        db.save(item);
                    }
                    adapter.refreshData(list);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (DbException e) {
                e.printStackTrace();
            }
            Log.e(TAG, "onNext: " + s);
        }
    };

    class TechnoAdapter extends BaseAdapter<NewsEntity> {

        @Override
        protected BaseViewHolder createViewHolder(Context context, ViewGroup parent) {
            return new CommonViewHolder(context, parent);
        }
    }
}