package com.news.function.news.common;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.news.R;
import com.news.base.BaseViewHolder;
import com.news.entity.NewsEntity;
import com.news.listener.OnItemClickListener;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/10/14.
 */
public class CommonViewHolder extends BaseViewHolder<NewsEntity> {
    private Context mContext;
    @Bind(R.id.card)
    CardView cardView;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.ctime)
    TextView ctime;
    @Bind(R.id.description)
    TextView description;
    @Bind(R.id.pic)
    ImageView pic;
    public CommonViewHolder(Context context, ViewGroup root) {
        super(context, root, R.layout.item_news_layout);
        this.mContext = context;
    }

    @Override
    protected void bindData(final NewsEntity itemValue, final int position, final OnItemClickListener listener) {
        title.setText(itemValue.getTitle());
        ctime.setText(itemValue.getCtime());
        description.setText("来自:"+itemValue.getDescription());
        Glide.with(mContext).load(itemValue.getPicUrl()).error(R.drawable.downfail).into(pic);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(itemValue,v.getId(),position);
            }
        });
    }
}
