package com.example.mytaobaounion.Utils;

import android.content.Context;
import android.widget.Toast;

import com.example.mytaobaounion.Base.BaseApplication;

public class ToastUtils {
    private static Toast mToast;

    public static void showToast(Context context,String tips){
        if(mToast==null){
            mToast=Toast.makeText(context,tips,Toast.LENGTH_SHORT);
        }
        else{
            mToast.setText(tips);
        }

        mToast.show();
    }

}
