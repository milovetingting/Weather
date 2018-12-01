package com.wangyz.weather.presenter;

import android.util.Log;

import com.wangyz.weather.base.BasePresenter;
import com.wangyz.weather.bean.db.City;
import com.wangyz.weather.bean.model.Air;
import com.wangyz.weather.bean.model.Sun;
import com.wangyz.weather.bean.model.Weather;
import com.wangyz.weather.contract.WeatherContract;
import com.wangyz.weather.model.WeatherModel;
import com.wangyz.weather.util.CacheUtil;

import org.litepal.LitePal;

import io.reactivex.MaybeObserver;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author wangyz
 * WeatherPresenter
 */
public class WeatherPresenter extends BasePresenter<WeatherContract.WeatherView> implements WeatherContract.WeatherPresenter {

    private static final String TAG = WeatherPresenter.class.getSimpleName();

    private WeatherContract.WeatherModel mModel;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public WeatherPresenter() {
        mModel = new WeatherModel();
    }

    private City model;

    @Override
    protected void detachView() {
        super.detachView();
        mCompositeDisposable.clear();
    }


    @Override
    public void autoUpdate(String city) {
        mModel.autoUpdate(city).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<City>() {
            @Override
            public void onSubscribe(Disposable d) {
                mCompositeDisposable.add(d);
            }

            @Override
            public void onNext(City city) {
                getView().onAutoUpdate(city);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError:" + e.getMessage());
                getView().onLoadFailed(e);
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete");
            }
        });
    }

    @Override
    public void loadWeather(String city) {
        mModel.loadWeather(city).firstElement().doOnSuccess(weather -> {
            CacheUtil.getInstance().putMemoryCache(city + "_weather", weather);
            CacheUtil.getInstance().putDiskCache(city + "_weather", weather);
            model = LitePal.where("name=?", city).findFirst(City.class);
            if (model != null && model.getUpdateTime() == 0) {
                model.setUpdateTime(System.currentTimeMillis());
                model.save();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MaybeObserver<Weather>() {

            @Override
            public void onSubscribe(Disposable d) {
                mCompositeDisposable.add(d);
            }

            @Override
            public void onSuccess(Weather weather) {
                getView().onLoadWeather(weather);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError:" + e.getMessage());
                getView().onSetCityModel(model);
                getView().onLoadFailed(e);
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete");
            }
        });
    }

    @Override
    public void refreshWeather(String city) {
        mModel.refreshWeather(city).doOnNext(weather -> {
            CacheUtil.getInstance().putMemoryCache(city + "_weather", weather);
            CacheUtil.getInstance().putDiskCache(city + "_weather", weather);
            model = LitePal.where("name=?", city).findFirst(City.class);
            if (model != null) {
                model.setUpdateTime(System.currentTimeMillis());
                model.save();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Weather>() {

            @Override
            public void onSubscribe(Disposable d) {
                mCompositeDisposable.add(d);
            }

            @Override
            public void onNext(Weather weather) {
                getView().onSetCityModel(model);
                getView().onRefreshWeather(weather);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError:" + e.getMessage());
                getView().onRefreshFailed(e);
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete");
            }
        });
    }

    @Override
    public void loadAir(String city) {
        mModel.loadAir(city).firstElement().doOnSuccess(air -> {
            CacheUtil.getInstance().putMemoryCache(city + "_air", air);
            CacheUtil.getInstance().putDiskCache(city + "_air", air);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MaybeObserver<Air>() {
            @Override
            public void onSubscribe(Disposable d) {
                mCompositeDisposable.add(d);
            }

            @Override
            public void onSuccess(Air air) {
                getView().onLoadAir(air);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError:" + e.getMessage());
                getView().onLoadFailed(e);
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete");
            }
        });
    }

    @Override
    public void refreshAir(String city) {
        mModel.refreshAir(city).doOnNext(air -> {
            CacheUtil.getInstance().putMemoryCache(city + "_air", air);
            CacheUtil.getInstance().putDiskCache(city + "_air", air);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Air>() {
            @Override
            public void onSubscribe(Disposable d) {
                mCompositeDisposable.add(d);
            }

            @Override
            public void onNext(Air air) {
                getView().onRefreshAir(air);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError:" + e.getMessage());
                getView().onRefreshFailed(e);
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete");
            }
        });
    }

    @Override
    public void loadSun(String city) {
        mModel.loadSun(city).firstElement().doOnSuccess(sun -> {
            CacheUtil.getInstance().putMemoryCache(city + "_sun", sun);
            CacheUtil.getInstance().putDiskCache(city + "_sun", sun);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MaybeObserver<Sun>() {
            @Override
            public void onSubscribe(Disposable d) {
                mCompositeDisposable.add(d);
            }

            @Override
            public void onSuccess(Sun sun) {
                getView().onLoadSun(sun);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError:" + e.getMessage());
                getView().onLoadFailed(e);
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete");
            }
        });
    }

    @Override
    public void refreshSun(String city) {
        mModel.refreshSun(city).doOnNext(sun -> {
            CacheUtil.getInstance().putMemoryCache(city + "_sun", sun);
            CacheUtil.getInstance().putDiskCache(city + "_sun", sun);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Sun>() {
            @Override
            public void onSubscribe(Disposable d) {
                mCompositeDisposable.add(d);
            }

            @Override
            public void onNext(Sun sun) {
                getView().onRefreshSun(sun);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError:" + e.getMessage());
                getView().onRefreshFailed(e);
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete");
            }
        });
    }


}
