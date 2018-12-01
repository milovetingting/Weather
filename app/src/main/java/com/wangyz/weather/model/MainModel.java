package com.wangyz.weather.model;

import android.annotation.TargetApi;
import android.os.Build;

import com.wangyz.weather.bean.db.City;
import com.wangyz.weather.contract.WeatherContract;

import org.litepal.LitePal;

import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.Observable;

/**
 * @author wangyz
 * 主界面相关业务处理。
 */
public class MainModel implements WeatherContract.MainModel {
    @Override
    public Observable<List<City>> loadCity() {
        return Observable.create(emitter -> {
            List<City> cityList = LitePal.findAll(City.class);
            emitter.onNext(cityList);
            emitter.onComplete();
        });
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public Observable<City> showCity(String city) {
        return Observable.create(emitter -> {
            City model = null;
            List<City> cityList = LitePal.findAll(City.class);
            if (!cityList.stream().anyMatch(c -> c.getName().equals(city))) {
                model = new City();
                model.setName(city);
            } else {
                model = cityList.stream().filter(c -> c.getName().equals(city)).findFirst().get();
            }
            emitter.onNext(model);
            emitter.onComplete();
        });
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public Observable<List<City>> saveCityList(final List<City> list) {
        return Observable.create(emitter -> {
            LitePal.deleteAll(City.class);
            List<City> collect = list.stream().map(c -> {
                c.assignBaseObjId(0);
                return c;
            }).collect(Collectors.toList());
            LitePal.saveAll(collect);
            List<City> cityList = LitePal.findAll(City.class);
            emitter.onNext(cityList);
            emitter.onComplete();
        });
    }
}
