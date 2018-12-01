package com.wangyz.weather.contract;

import android.widget.EditText;

import com.wangyz.weather.bean.model.Air;
import com.wangyz.weather.bean.model.City;
import com.wangyz.weather.bean.model.Sun;
import com.wangyz.weather.bean.model.Weather;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author wangyz
 * 接口类
 */
public class WeatherContract {

    /**
     * MainModel
     */
    public interface MainModel {

        /**
         * 获取当前选择的城市
         *
         * @return
         */
        Observable<List<com.wangyz.weather.bean.db.City>> loadCity();

        /**
         * 展示城市
         *
         * @param city
         * @return
         */
        Observable<com.wangyz.weather.bean.db.City> showCity(String city);

        /**
         * 保存城市列表
         *
         * @param list
         * @return
         */
        Observable<List<com.wangyz.weather.bean.db.City>> saveCityList(List<com.wangyz.weather.bean.db.City> list);
    }

    /**
     * MainView
     */
    public interface MainView {

        /**
         * 获取当前选择的城市完成
         *
         * @param cityList
         */
        void onLoadCity(List<com.wangyz.weather.bean.db.City> cityList);

        /**
         * 展示城市完成
         *
         * @param city
         */
        void onShowCity(com.wangyz.weather.bean.db.City city);

        /**
         * 保存城市列表完成
         *
         * @param list
         */
        void onSaveCityList(List<com.wangyz.weather.bean.db.City> list);
    }

    /**
     * MainPresenter
     */
    public interface MainPresenter {

        /**
         * 获取当前选择的城市
         */
        void loadCity();

        /**
         * 展示城市
         *
         * @param city
         */
        void showCity(String city);

        /**
         * 保存城市列表
         *
         * @param list
         */
        void saveCityList(List<com.wangyz.weather.bean.db.City> list);
    }

    /**
     * CityModel
     */
    public interface CityModel {

        /**
         * 搜索城市
         *
         * @param location
         * @return
         */
        Observable<City> searchCity(String location);

        /**
         * 获取热门城市
         *
         * @return
         */
        Observable<City> loadTopCity();

        /**
         * 保存城市到数据库
         *
         * @param city
         * @return
         */
        Observable<com.wangyz.weather.bean.db.City> saveCity(com.wangyz.weather.bean.db.City city);

    }

    /**
     * CityView
     */
    public interface CityView {

        /**
         * 加载失败
         *
         * @param e
         */
        void onLoadFailed(Throwable e);

        /**
         * 搜索城市完成
         *
         * @param city
         */
        void onSearchCityComplete(City city);

        /**
         * 获取热门城市完成
         *
         * @param city
         */
        void onLoadTopCityComplete(City city);

        /**
         * 保存城市到数据库完成
         *
         * @param city
         */
        void onSaveCityComplete(com.wangyz.weather.bean.db.City city);

    }

    /**
     * CityPresenter
     */
    public interface CityPresenter {

        /**
         * 搜索城市
         *
         * @param keyWord
         * @param et
         */
        void searchCity(String keyWord, EditText et);

        /**
         * 获取热门城市
         */
        void loadTopCity();

        /**
         * 保存城市到数据库
         *
         * @param city
         */
        void saveCity(com.wangyz.weather.bean.db.City city);

    }

    /**
     * WeatherModel
     */
    public interface WeatherModel {

        /**
         * 自动更新
         *
         * @param city
         * @return
         */
        Observable<com.wangyz.weather.bean.db.City> autoUpdate(String city);

        /**
         * 获取天气
         *
         * @param city
         * @return
         */
        Observable<Weather> loadWeather(String city);

        /**
         * 刷新天气
         *
         * @param city
         * @return
         */
        Observable<Weather> refreshWeather(String city);

        /**
         * 获取空气质量信息
         *
         * @param city
         * @return
         */
        Observable<Air> loadAir(String city);

        /**
         * 刷新空气质量信息
         *
         * @param city
         * @return
         */
        Observable<Air> refreshAir(String city);

        /**
         * 获取日出日落信息
         *
         * @param city
         * @return
         */
        Observable<Sun> loadSun(String city);

        /**
         * 刷新日出日落信息
         *
         * @param city
         * @return
         */
        Observable<Sun> refreshSun(String city);

    }

    /**
     * WeatherView
     */
    public interface WeatherView {

        /**
         * 加载失败
         *
         * @param e
         */
        void onLoadFailed(Throwable e);

        /**
         * 刷新失败
         *
         * @param e
         */
        void onRefreshFailed(Throwable e);

        /**
         * 自动更新
         *
         * @param city
         */
        void onAutoUpdate(com.wangyz.weather.bean.db.City city);

        /**
         * 设置CityModel
         *
         * @param city
         */
        void onSetCityModel(com.wangyz.weather.bean.db.City city);

        /**
         * 获取天气完成
         *
         * @param weather
         */
        void onLoadWeather(Weather weather);

        /**
         * 刷新天气完成
         *
         * @param weather
         */
        void onRefreshWeather(Weather weather);

        /**
         * 获取空气质量信息完成
         *
         * @param air
         */
        void onLoadAir(Air air);

        /**
         * 刷新空气质量信息完成
         *
         * @param air
         */
        void onRefreshAir(Air air);

        /**
         * 获取日出日落信息
         *
         * @param sun
         */
        void onLoadSun(Sun sun);

        /**
         * 刷新日出日落信息
         *
         * @param sun
         */
        void onRefreshSun(Sun sun);

    }

    /**
     * WeatherPresenter
     */
    public interface WeatherPresenter {

        /**
         * 自动更新
         *
         * @param city
         */
        void autoUpdate(String city);

        /**
         * 获取天气
         *
         * @param city
         */
        void loadWeather(String city);

        /**
         * 刷新天气
         *
         * @param city
         */
        void refreshWeather(String city);

        /**
         * 获取空气质量信息
         *
         * @param city
         */
        void loadAir(String city);

        /**
         * 刷新空气质量信息
         *
         * @param city
         */
        void refreshAir(String city);

        /**
         * 获取日出日落信息
         *
         * @param city
         */
        void loadSun(String city);

        /**
         * 刷新日出日落信息
         *
         * @param city
         */
        void refreshSun(String city);

    }

}
