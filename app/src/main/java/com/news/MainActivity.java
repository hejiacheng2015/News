package com.news;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.news.base.BaseActivity;
import com.news.function.card.SZCardActivity;
import com.news.function.news.AppleFragment;
import com.news.function.news.ChinaFragment;
import com.news.function.news.EnjoyFragment;
import com.news.function.news.SocialFragment;
import com.news.function.news.SportFragment;
import com.news.function.news.RecomFragment;
import com.news.function.news.TechnoFragment;

import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;

public class MainActivity extends BaseActivity {

    @Bind(R.id.view_pager)
    ViewPager mViewPager;

    @Bind(R.id.tabLayout)
    TabLayout mTabLayout;

    @Bind(R.id.tb_toolbar)
    Toolbar toolbar;

    @Bind(R.id.navigation_view)
    NavigationView mNavigationView;

    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    //初始化TabLayout的title数据集
    String []titles = {"推荐","体育","社会","科技","娱乐","国内","苹果"};
    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_chrome_reader_mode_white_36dp));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //对NavigationView添加item的监听事件
        mNavigationView.setNavigationItemSelectedListener(naviListener);
        //开启应用默认关闭DrawerLayout
        mDrawerLayout.closeDrawer(mNavigationView);

        //设置TabLayout
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mViewPager = (ViewPager) this.findViewById(R.id.view_pager);
    }

    @Override
    protected void initData() {
//        初始化ViewPager的数据集
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new RecomFragment());
        fragments.add(new SportFragment());
        fragments.add(new SocialFragment());
        fragments.add(new TechnoFragment());
        fragments.add(new EnjoyFragment());
        fragments.add(new ChinaFragment());
        fragments.add(new AppleFragment());

        //创建ViewPager的adapter
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private NavigationView.OnNavigationItemSelectedListener naviListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {
            //点击NavigationView中定义的menu item时触发反应
            switch (menuItem.getItemId()) {
                case R.id.szcard:
                    startActivity(new Intent(MainActivity.this, SZCardActivity.class));
                    break;
                case R.id.menu_share:
                    mViewPager.setCurrentItem(1);
                    break;
                case R.id.menu_agenda:
                    mViewPager.setCurrentItem(2);
                    break;
            }
            //关闭DrawerLayout回到主界面选中的tab的fragment页
            mDrawerLayout.closeDrawer(mNavigationView);
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 為了讓 Toolbar 的 Menu 有作用，這邊的程式不可以拿掉
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public class FragmentAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> mFragments;
        private String[] mTitles;

        public FragmentAdapter(FragmentManager fm, List<Fragment> fragments, String[] titles) {
            super(fm);
            mFragments = fragments;
            mTitles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addCategory(Intent.CATEGORY_HOME);
        startActivity(i);
    }
}
