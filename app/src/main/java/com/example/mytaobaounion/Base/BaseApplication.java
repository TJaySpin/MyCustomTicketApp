package com.example.mytaobaounion.Base;


import android.app.Application;
import android.content.Context;

import com.example.mytaobaounion.Utils.LogUtils;


//为了获取context，注意要在清单文件中注册
public class BaseApplication extends Application {
    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext=getBaseContext();
    }




    public static Context getAppContext() {
        return appContext;
    }

}
