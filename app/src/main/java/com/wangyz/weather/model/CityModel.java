package com.wangyz.weather.model;

import android.annotation.TargetApi;
import android.os.Build;

import com.wangyz.weather.ConstantValue;
import com.wangyz.weather.api.WeatherApi;
import com.wangyz.weather.bean.model.City;
import com.wangyz.weather.contract.WeatherContract;

import org.litepal.LitePal;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author wangyz
 * 城市相关业务处理
 */
public class CityModel implements WeatherContract.CityModel {

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public Observable<City> loadTopCity() {
        final List<com.wangyz.weather.bean.db.City> cities = LitePal.findAll(com.wangyz.weather.bean.db.City.class);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ConstantValue.API_URL_SEARCH_CITY).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).addConverterFactory(GsonConverterFactory.create()).build();
        WeatherApi weatherAPI = retrofit.create(WeatherApi.class);
        return weatherAPI.loadTopCity("cn", 35, "cn", ConstantValue.KEY).map(city -> {
            city.getHeWeather6().get(0).getBasic().removeIf(b -> cities.stream().anyMatch(c -> c.getName().equals(b.getLocation())));
            return city;
        });
    }

    @Override
    public Observable<com.wangyz.weather.bean.db.City> saveCity(final com.wangyz.weather.bean.db.City city) {
        return Observable.create(emitter -> {
            int count = LitePal.where("name=?", city.getName()).count(com.wangyz.weather.bean.db.City.class);
            if (count <= 0) {
                city.save();
            }
            emitter.onNext(city);
            emitter.onComplete();
        });
    }


    @Override
    public Observable<City> searchCity(String location) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ConstantValue.API_URL_SEARCH_CITY).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).addConverterFactory(GsonConverterFactory.create()).build();
        WeatherApi weatherAPI = retrofit.create(WeatherApi.class);
        return weatherAPI.searchCity(location, "match", "cn", 20, "cn", ConstantValue.KEY);
    }
}
