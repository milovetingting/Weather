package com.wangyz.weather;

import android.app.Application;

import com.blankj.utilcode.util.Utils;

import org.litepal.LitePal;

/**
 * @author wangyz
 * Application
 */
public class WeatherApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
        Utils.init(this);
    }
}
