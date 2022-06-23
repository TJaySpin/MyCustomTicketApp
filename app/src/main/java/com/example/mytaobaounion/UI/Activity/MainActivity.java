package com.example.mytaobaounion.UI.Activity;

import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mytaobaounion.Base.BaseActivity;
import com.example.mytaobaounion.Base.BaseFragment;
import com.example.mytaobaounion.R;
import com.example.mytaobaounion.UI.Fragment.HomeFragment;
import com.example.mytaobaounion.UI.Fragment.RedpacketFragment;
import com.example.mytaobaounion.UI.Fragment.SearchFragment;
import com.example.mytaobaounion.UI.Fragment.SelectedFragment;
import com.example.mytaobaounion.Utils.LogUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements IMainActivity{

    @BindView(R.id.main_navigation_bar)
    public BottomNavigationView bottomNavigationView;
    private HomeFragment homeFragment;
    private SelectedFragment selectedFragment;
    private RedpacketFragment redpacketFragment;
    private SearchFragment searchFragment;
    private FragmentManager fragmentManager;
    private BaseFragment lastFragment;


    protected void initView(){
        initFragment();
    }

    private void initFragment() {
        homeFragment=new HomeFragment();
        selectedFragment=new SelectedFragment();
        redpacketFragment=new RedpacketFragment();
        searchFragment=new SearchFragment();
        fragmentManager=getSupportFragmentManager();
        //默认刚启动就显示首页Fragment
        switchFragment(homeFragment);
    }




    @Override
    protected void initEvent() {
        initListener();
    }

    private void initListener() {
        //设置导航栏切换监听，根据menu中item的id，跳转到对应的内容Fragment
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.home){
                    switchFragment(homeFragment);
                    LogUtils.d(MainActivity.class,item.getTitle().toString());
                }
                else if(item.getItemId()==R.id.selected){
                    switchFragment(selectedFragment);
                    LogUtils.i(MainActivity.class,item.getTitle().toString());
                }
                else if(item.getItemId()==R.id.red_packet){
                    switchFragment(redpacketFragment);
                    LogUtils.w(MainActivity.class,item.getTitle().toString());
                }
                else if(item.getItemId()==R.id.search){
                    switchFragment(searchFragment);
                    LogUtils.e(MainActivity.class,item.getTitle().toString());
                }
                //必须返回true，直接消耗该点击事件
                return true;
            }
        });
    }

    //将对应的内容Fragment添加进activity_main中的FrameLayout（即R.id.home_framelayout），通过show和hide显示（这是为了避免TabLayout切换后前一个内容Fragment被销毁）
    private void switchFragment(BaseFragment baseFragment){
        //防止连点两次页面空白的情况，因此记录了上一个Fragment，如果连点同一个页面两次就直接退出
        if(lastFragment==baseFragment){
            return;
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if(!baseFragment.isAdded()){
            transaction.add(R.id.home_framelayout,baseFragment);
        }
        else{
            transaction.show(baseFragment);
        }

        //这里如果不处理，连点两次就会因为hide导致页面空白
        if(lastFragment!=null){
            transaction.hide(lastFragment);
        }
        lastFragment=baseFragment;
        transaction.commit();
    }



    @Override
    protected void initPresenter() {
    }



    //实现自IMainActivity中的方法，因为bottomNavigationView在MainActivity中，所以只能由MainActivity实现
    @Override
    public void switch2search() {
        bottomNavigationView.setSelectedItemId(R.id.search);
    }


    @Override
    protected int getResourceId() {
        return R.layout.activity_main;
    }
}