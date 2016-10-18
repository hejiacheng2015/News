package com.news.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/13.
 */
public abstract class BaseFrament extends Fragment {
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(getLayoutResource(), container, false);
        ButterKnife.bind(this, view);
        initView();
        initData();
        return view;
    }

    abstract protected int getLayoutResource();
    abstract protected void initView();
    abstract protected void initData();
}
