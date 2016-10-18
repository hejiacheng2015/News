package com.news.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/10/18.
 */
public class T {
    private static Toast toast;
    public static void Shows(Context mthis,String Message){
        if (toast == null){
            toast = Toast.makeText(mthis,Message,Toast.LENGTH_SHORT);
        }else {
            toast.setText(Message);
        }
        toast.show();
    }
}
