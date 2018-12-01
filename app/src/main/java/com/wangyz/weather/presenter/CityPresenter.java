package com.wangyz.weather.presenter;

import android.util.Log;
import android.widget.EditText;

import com.jakewharton.rxbinding3.widget.RxTextView;
import com.wangyz.weather.base.BasePresenter;
import com.wangyz.weather.bean.model.City;
import com.wangyz.weather.contract.WeatherContract;
import com.wangyz.weather.model.CityModel;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author wangyz
 * CityPresenter
 */
public class CityPresenter extends BasePresenter<WeatherContract.CityView> implements WeatherContract.CityPresenter {

    private static final String TAG = CityPresenter.class.getSimpleName();

    private WeatherContract.CityModel mModel;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private Observable<City> cityObservable;

    public CityPresenter() {
        mModel = new CityModel();
    }

    @Override
    protected void detachView() {
        super.detachView();
        mCompositeDisposable.clear();
        if (cityObservable != null) {
            cityObservable.unsubscribeOn(AndroidSchedulers.mainThread());
        }
    }

    @Override
    public void loadTopCity() {
        mModel.loadTopCity().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<City>() {
            @Override
            public void onSubscribe(Disposable d) {
                mCompositeDisposable.add(d);
            }

            @Override
            public void onNext(City city) {
                getView().onLoadTopCityComplete(city);
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
    public void saveCity(com.wangyz.weather.bean.db.City city) {
        mModel.saveCity(city).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<com.wangyz.weather.bean.db.City>() {
            @Override
            public void onSubscribe(Disposable d) {
                mCompositeDisposable.add(d);
            }

            @Override
            public void onNext(com.wangyz.weather.bean.db.City city) {
                getView().onSaveCityComplete(city);
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
    public void searchCity(String keyWord, EditText et) {
        cityObservable = RxTextView.textChanges(et).debounce(500, TimeUnit.MILLISECONDS).filter(charSequence -> charSequence.toString().trim().length() > 0).switchMap((Function<CharSequence, ObservableSource<City>>) charSequence -> mModel.searchCity(charSequence.toString())).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        cityObservable.subscribe(new Observer<City>() {
            @Override
            public void onSubscribe(Disposable d) {
                mCompositeDisposable.add(d);
            }

            @Override
            public void onNext(City city) {
                getView().onSearchCityComplete(city);
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
}
