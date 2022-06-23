package com.example.mytaobaounion.UI.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.mytaobaounion.R;
import com.example.mytaobaounion.UI.Custom.TextFlowLayout;
import com.example.mytaobaounion.Utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestActivity2 extends AppCompatActivity{

    @BindView(R.id.my_TextFlow)
    public TextFlowLayout textFlowLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);

        ButterKnife.bind(this);
        List<String> testList = new ArrayList<>();
        testList.add("电脑");
        testList.add("电脑显示器");
        testList.add("Nuxt.js");
        testList.add("Vue.js课程");
        testList.add("机械键盘");
        testList.add("滑板鞋");

        textFlowLayout.setTextList(testList);
        textFlowLayout.setFlowTextClickListener(new TextFlowLayout.onFlowTextClickListener() {
            @Override
            public void onFlowTextClick(String text) {
                LogUtils.i(TestActivity2.class,"wdnmd我被点击了");
            }
        });

    }
}