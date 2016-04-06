package com.stephen.hotfix;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Stephen on 4/5/16.
 */
public class BugClass {
    public static void echo(Context context, String msg) {
        String echoStr = String.format("Hello from BugClass: %s", msg);
        Log.d("BugClass", echoStr);
        Toast.makeText(context, echoStr, Toast.LENGTH_SHORT).show();
    }
}
