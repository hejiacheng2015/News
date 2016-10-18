package com.news.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.news.R;
import com.news.function.news.RecomFragment;
import com.news.listener.OnItemClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Adapter基类.
 * 适用于只有单个Item的RecyclerView.
 *
 * Created by lypeer on 16-5-24.
 */
public abstract class BaseAdapter<V> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "BaseAdapter";
    /**
     * item类型
     */
    public static final int ITEM_TYPE_CONTENT = 1;
    public static final int ITEM_TYPE_BOTTOM = 2;
    public static int ITEM_TYPE = 0;

    /**
     * 装载了每个Item的Value的列表
     */
    private List<V> mValueList;

    /**
     * 我写的一个接口，通过回调分发点击事件
     */
    private OnItemClickListener<V> mOnItemClickListener;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==ITEM_TYPE_CONTENT){
            return createViewHolder(parent.getContext(), parent);
        }else if(viewType==ITEM_TYPE_BOTTOM){
            return new BottomViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_layout, parent, false));
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")//一定会是BaseViewHolder的子类，因为createViewHolder()的返回值
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(ITEM_TYPE==ITEM_TYPE_CONTENT){
            ((BaseViewHolder) holder).setData(mValueList.get(position), position, mOnItemClickListener);
        }else if(ITEM_TYPE==ITEM_TYPE_BOTTOM){

        }
    }

    @Override
    public int getItemViewType(int position) {
        if(getItemCount()==(position+1)){
            ITEM_TYPE = ITEM_TYPE_BOTTOM;
            return ITEM_TYPE_BOTTOM;
        }else {
            ITEM_TYPE = ITEM_TYPE_CONTENT;
            return ITEM_TYPE_CONTENT;
        }
    }

    /**
     * 设置每个Item的点击事件
     * @param listener
     */
    public void setOnClickListener(OnItemClickListener<V> listener) {
        this.mOnItemClickListener = listener;
    }

    /**
     * 刷新数据
     * @param valueList 新的数据列表
     */
    public void refreshData(List<V> valueList) {
        this.mValueList = valueList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mValueList == null ? 0 : mValueList.size()+1;
    }

    /**
     * 生成ViewHolder
     * @param context
     * @param parent
     * @return
     */
    protected abstract BaseViewHolder createViewHolder(Context context, ViewGroup parent);

    /**
     * 底部 ViewHolder
     */
    public static class BottomViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_foot)
        TextView tv_foot;
        @Bind(R.id.progress_foot)
        ProgressBar progressBar_foot;
        public BottomViewHolder(View itemView) {
                super(itemView);
                //这里使用了ButterKnife来进行控件的绑定
                ButterKnife.bind(this, itemView);
            }
        }
    }

