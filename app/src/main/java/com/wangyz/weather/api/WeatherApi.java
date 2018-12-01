package com.wangyz.weather.api;

import com.wangyz.weather.bean.model.Air;
import com.wangyz.weather.bean.model.City;
import com.wangyz.weather.bean.model.Sun;
import com.wangyz.weather.bean.model.Weather;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author wangyz
 * WeatherApi
 */
public interface WeatherApi {

    /**
     * 获取热门城市
     *
     * @param group
     * @param number
     * @param lang
     * @param key
     * @return
     */
    @POST("/top")
    Observable<City> loadTopCity(@Query("group") String group, @Query("number") int number, @Query("lang") String lang, @Query("key") String key);

    /**
     * 搜索城市
     *
     * @param location
     * @param mode
     * @param group
     * @param number
     * @param lang
     * @param key
     * @return
     */
    @POST("/find")
    Observable<City> searchCity(@Query("location") String location, @Query("mode") String mode, @Query("group") String group, @Query("number") int number, @Query("lang") String lang, @Query("key") String key);

    /**
     * 签名方式获取天气
     *
     * @param location
     * @param lang
     * @param unit
     * @param username
     * @param t
     * @param sign
     * @return
     */
    @POST("/s6/weather")
    Observable<Weather> loadWeatherWithSign(@Query("location") String location, @Query("lang") String lang, @Query("unit") String unit, @Query("username") String username, @Query("t") String t, @Query("sign") String sign);

    /**
     * 签名方式获取空气质量信息
     *
     * @param location
     * @param lang
     * @param unit
     * @param username
     * @param t
     * @param sign
     * @return
     */
    @POST("/s6/air/now")
    Observable<Air> loadAirWithSign(@Query("location") String location, @Query("lang") String lang, @Query("unit") String unit, @Query("username") String username, @Query("t") String t, @Query("sign") String sign);

    /**
     * 签名方式获取日出日落信息
     *
     * @param location
     * @param lang
     * @param unit
     * @param username
     * @param t
     * @param sign
     * @return
     */
    @POST("/s6/solar/sunrise-sunset")
    Observable<Sun> loadSunWithSign(@Query("location") String location, @Query("lang") String lang, @Query("unit") String unit, @Query("username") String username, @Query("t") String t, @Query("sign") String sign);
}
