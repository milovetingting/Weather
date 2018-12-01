package com.wangyz.weather.model;

import com.wangyz.weather.ConstantValue;
import com.wangyz.weather.api.WeatherApi;
import com.wangyz.weather.bean.db.City;
import com.wangyz.weather.bean.model.Air;
import com.wangyz.weather.bean.model.Sun;
import com.wangyz.weather.bean.model.Weather;
import com.wangyz.weather.contract.WeatherContract;
import com.wangyz.weather.util.CacheUtil;
import com.wangyz.weather.util.SignUtil;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.HashMap;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author wangyz
 * WeatherModel
 */
public class WeatherModel implements WeatherContract.WeatherModel {

    @Override
    public Observable<City> autoUpdate(String city) {
        return Observable.create(emitter -> {
            City model = LitePal.where("name=?", city).findFirst(City.class);
            emitter.onNext(model);
            emitter.onComplete();
        });
    }

    @Override
    public Observable<Weather> loadWeather(final String city) {

        Observable<Weather> memoryObservable = Observable.create(emitter -> {
            Weather weather = CacheUtil.getInstance().getMemoryCache(city + "_weather");
            if (weather != null) {
                emitter.onNext(weather);
            } else {
                emitter.onComplete();
            }
        });

        Observable<Weather> diskObservable = Observable.create(emitter -> {
            Weather weather = CacheUtil.getInstance().getDiskCache(city + "_weather");
            if (weather != null) {
                emitter.onNext(weather);
            } else {
                emitter.onComplete();
            }
        });

        Observable<Weather> weatherObservable = doLoadWeather(city);

        return Observable.concat(memoryObservable, diskObservable, weatherObservable);
    }

    @Override
    public Observable<Weather> refreshWeather(String city) {
        return doLoadWeather(city);
    }

    @Override
    public Observable<Air> loadAir(String city) {

        Observable<Air> memoryObservable = Observable.create(emitter -> {
            Air air = CacheUtil.getInstance().getMemoryCache(city + "_air");
            if (air != null) {
                emitter.onNext(air);
            } else {
                emitter.onComplete();
            }
        });

        Observable<Air> diskObservable = Observable.create(emitter -> {
            Air air = CacheUtil.getInstance().getDiskCache(city + "_air");
            if (air != null) {
                emitter.onNext(air);
            } else {
                emitter.onComplete();
            }
        });

        Observable<Air> airObservable = doLoadAir(city);

        return Observable.concat(memoryObservable, diskObservable, airObservable);
    }

    @Override
    public Observable<Air> refreshAir(String city) {
        return doLoadAir(city);
    }

    @Override
    public Observable<Sun> loadSun(String city) {

        Observable<Sun> memoryObservable = Observable.create(emitter -> {
            Sun sun = CacheUtil.getInstance().getMemoryCache(city + "_sun");
            if (sun != null) {
                emitter.onNext(sun);
            } else {
                emitter.onComplete();
            }
        });

        Observable<Sun> diskObservable = Observable.create(emitter -> {
            Sun sun = CacheUtil.getInstance().getDiskCache(city + "_sun");
            if (sun != null) {
                emitter.onNext(sun);
            } else {
                emitter.onComplete();
            }
        });

        Observable<Sun> sunObservable = doLoadSun(city);

        return Observable.concat(memoryObservable, diskObservable, sunObservable);
    }

    @Override
    public Observable<Sun> refreshSun(String city) {
        return doLoadSun(city);
    }

    private Observable<Weather> doLoadWeather(String city) {
        HashMap<String, String> map = new HashMap<>();
        map.put("location", city);
        map.put("lang", "cn");
        map.put("unit", "m");
        map.put("username", ConstantValue.USERNAME);
        String t = System.currentTimeMillis() / 1000 + "";
        map.put("t", t);
        String signature = "";
        try {
            signature = SignUtil.getSignature(map, ConstantValue.KEY);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ConstantValue.API_URL_WEATHER).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).addConverterFactory(GsonConverterFactory.create()).build();
        WeatherApi weatherAPI = retrofit.create(WeatherApi.class);
        Observable<Weather> weatherObservable = weatherAPI.loadWeatherWithSign(city, "cn", "m", ConstantValue.USERNAME, t, signature);
        return weatherObservable;
    }

    private Observable<Air> doLoadAir(String city) {
        HashMap<String, String> map = new HashMap<>();
        map.put("location", city);
        map.put("lang", "cn");
        map.put("unit", "m");
        map.put("username", ConstantValue.USERNAME);
        String t = System.currentTimeMillis() / 1000 + "";
        map.put("t", t);
        String signature = "";
        try {
            signature = SignUtil.getSignature(map, ConstantValue.KEY);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ConstantValue.API_URL_WEATHER).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).addConverterFactory(GsonConverterFactory.create()).build();
        WeatherApi weatherAPI = retrofit.create(WeatherApi.class);
        Observable<Air> airObservable = weatherAPI.loadAirWithSign(city, "cn", "m", ConstantValue.USERNAME, t, signature);
        return airObservable;
    }

    private Observable<Sun> doLoadSun(String city) {
        HashMap<String, String> map = new HashMap<>();
        map.put("location", city);
        map.put("lang", "cn");
        map.put("unit", "m");
        map.put("username", ConstantValue.USERNAME);
        String t = System.currentTimeMillis() / 1000 + "";
        map.put("t", t);
        String signature = "";
        try {
            signature = SignUtil.getSignature(map, ConstantValue.KEY);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ConstantValue.API_URL_WEATHER).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).addConverterFactory(GsonConverterFactory.create()).build();
        WeatherApi weatherAPI = retrofit.create(WeatherApi.class);
        Observable<Sun> sunObservable = weatherAPI.loadSunWithSign(city, "cn", "m", ConstantValue.USERNAME, t, signature);
        return sunObservable;
    }
}
