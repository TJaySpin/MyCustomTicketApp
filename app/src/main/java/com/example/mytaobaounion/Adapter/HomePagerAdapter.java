package com.example.mytaobaounion.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.mytaobaounion.Model.Domain.Catagories;
import com.example.mytaobaounion.UI.Fragment.HomePagerFragment;

import java.util.ArrayList;
import java.util.List;

public class HomePagerAdapter extends FragmentPagerAdapter {
    //为什么每个Adapter中都是创建List储存数据？因为官方api文档中的json数据，全都是存在List里
    private List<Catagories.DataDTO> categoriesList = new ArrayList<>();

    public HomePagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return categoriesList.get(position).getTitle();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        //FragmentAdapter的特殊方法，返回值是Fragment，Tablayout里每个item对应的一个子HomePagerFragment
        HomePagerFragment homePagerFragment = HomePagerFragment.getInstance(categoriesList.get(position));
        return homePagerFragment;
    }

    @Override
    public int getCount() {
        return categoriesList.size();
    }


    //专门创建的方法用于显示数据，在View层的Fragment中调用，项目中基本上所有Adapter都是这个模式
    public void setCategoriesData(Catagories categories) {
        categoriesList.clear();
        List<Catagories.DataDTO> data = categories.getData();
        categoriesList.addAll(data);
        notifyDataSetChanged();
    }
}
