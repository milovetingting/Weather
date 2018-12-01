package com.wangyz.weather.presenter;

import android.util.Log;

import com.wangyz.weather.base.BasePresenter;
import com.wangyz.weather.bean.db.City;
import com.wangyz.weather.contract.WeatherContract;
import com.wangyz.weather.model.MainModel;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author wangyz
 * MainPresenter
 */
public class MainPresenter extends BasePresenter<WeatherContract.MainView> implements WeatherContract.MainPresenter {

    private static final String TAG = MainPresenter.class.getSimpleName();

    private WeatherContract.MainModel mModel;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public MainPresenter() {
        mModel = new MainModel();
    }

    @Override
    protected void detachView() {
        super.detachView();
        mCompositeDisposable.clear();
    }

    @Override
    public void loadCity() {
        mModel.loadCity().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<City>>() {
            @Override
            public void onSubscribe(Disposable d) {
                mCompositeDisposable.add(d);
            }

            @Override
            public void onNext(List<City> cityList) {
                getView().onLoadCity(cityList);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError:" + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete");
            }
        });
    }

    @Override
    public void showCity(String city) {
        mModel.showCity(city).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<City>() {
            @Override
            public void onSubscribe(Disposable d) {
                mCompositeDisposable.add(d);
            }

            @Override
            public void onNext(City city) {
                getView().onShowCity(city);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError:" + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete");
            }
        });
    }

    @Override
    public void saveCityList(List<City> list) {
        mModel.saveCityList(list).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<City>>() {
            @Override
            public void onSubscribe(Disposable d) {
                mCompositeDisposable.add(d);
            }

            @Override
            public void onNext(List<City> list) {
                getView().onSaveCityList(list);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError:" + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete");
            }
        });
    }
}
