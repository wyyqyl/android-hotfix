package com.stephen.hotfix;

import android.content.Context;
import android.os.Handler;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Collections;

/**
 * Created by Stephen on 3/30/16.
 */
public class Utils {
    private static final String PATCH_DEX_NAME = "patch.dex";

    public static void Do(final Context context) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {

                // BugClass.echo(context, "called from 1");

                // Hot patch
                File dexPath = new File(context.getDir("dex", Context.MODE_PRIVATE), PATCH_DEX_NAME);
                Utils.prepareDex(context, dexPath, PATCH_DEX_NAME);
                MultiDex.install(context, Collections.singletonList(dexPath));

                BugClass.echo(context, "called from 2");

                try {
                    Class<?> clazz = Class.forName("com.stephen.hotfix.BugClass");
                    Method echo = clazz.getDeclaredMethod("echo", Context.class, String.class);
                    echo.invoke(null, context, "called from 3");
                } catch (Throwable e) {
                    e.printStackTrace();
                }

                BugClass.echo(context, "called from 4");
            }
        });
    }

    private static final int BUF_SIZE = 2048;
    private static boolean prepareDex(Context context, File dexInternalStoragePath, String dex_file) {
        BufferedInputStream bis = null;
        OutputStream dexWriter = null;

        try {
            bis = new BufferedInputStream(context.getAssets().open(dex_file));
            dexWriter = new BufferedOutputStream(new FileOutputStream(dexInternalStoragePath));
            byte[] buf = new byte[BUF_SIZE];
            int len;
            while ((len = bis.read(buf, 0, BUF_SIZE)) > 0) {
                dexWriter.write(buf, 0, len);
            }
            dexWriter.close();
            bis.close();
            return true;
        } catch (IOException e) {
            if (dexWriter != null) {
                try {
                    dexWriter.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
            return false;
        }
    }
}
