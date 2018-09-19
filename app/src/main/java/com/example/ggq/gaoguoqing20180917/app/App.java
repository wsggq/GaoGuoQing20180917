package com.example.ggq.gaoguoqing20180917.app;

import android.app.Application;

import com.example.ggq.gaoguoqing20180917.utils.CrashHandler;
import com.facebook.drawee.backends.pipeline.Fresco;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        // 异常处理，不需要处理时注释掉这两句即可！
        CrashHandler crashHandler = CrashHandler.getInstance();
        // 注册crashHandler
        crashHandler.init(getApplicationContext());
    }
}
