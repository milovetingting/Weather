package com.wangyz.weather.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.wangyz.weather.ConstantValue;
import com.wangyz.weather.R;
import com.wangyz.weather.adapter.DailyWeatherAdapter;
import com.wangyz.weather.adapter.HourlyWeatherAdapter;
import com.wangyz.weather.base.BaseFragment;
import com.wangyz.weather.bean.db.City;
import com.wangyz.weather.bean.model.Air;
import com.wangyz.weather.bean.model.Sun;
import com.wangyz.weather.bean.model.Weather;
import com.wangyz.weather.contract.WeatherContract;
import com.wangyz.weather.custom.CircleView;
import com.wangyz.weather.custom.SunView;
import com.wangyz.weather.presenter.WeatherPresenter;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;

/**
 * @author wangyz
 * WeatherFragment
 */
public class WeatherFragment extends BaseFragment<WeatherContract.WeatherView, WeatherPresenter> implements WeatherContract.WeatherView {

    private static final String TAG = WeatherFragment.class.getSimpleName();

    @BindView(R.id.weather_refresh)
    SmartRefreshLayout mSmartRefreshLayout;

    @BindView(R.id.weather_scroll_view)
    ScrollView mScrollView;

    @BindView(R.id.weather_cl)
    ConstraintLayout mLayout;

    @BindView(R.id.weather_tv_current_temperature)
    TextView mCurrentTemperature;

    @BindView(R.id.weather_tv_current_weather)
    TextView mCurrentWeather;

    @BindView(R.id.weather_tv_today_temperature)
    TextView mTodayTemperature;

    @BindView(R.id.weather_tv_today_air)
    TextView mTodayAir;

    @BindView(R.id.weather_tv_last_update)
    TextView mLastUpdate;

    @BindView(R.id.weather_iv_api_logo)
    ImageView mApiLogo;

    @BindView(R.id.weather_rv_hourly)
    RecyclerView mHourlyRecyclerView;

    @BindView(R.id.weather_lv_daily)
    ListView mDailyListView;

    @BindView(R.id.weather_cv_air)
    CircleView mAir;

    @BindView(R.id.weather_tv_pm10_value)
    TextView mPm10;

    @BindView(R.id.weather_tv_pm25_value)
    TextView mPm25;

    @BindView(R.id.weather_tv_no2_value)
    TextView mNo2;

    @BindView(R.id.weather_tv_so2_value)
    TextView mSo2;

    @BindView(R.id.weather_tv_o3_value)
    TextView mO3;

    @BindView(R.id.weather_tv_co_value)
    TextView mCo;

    @BindView(R.id.weather_cv_comfortable)
    CircleView mComfortable;

    @BindView(R.id.weather_tv_temp_value)
    TextView mTemp;

    @BindView(R.id.weather_tv_uv_value)
    TextView mUv;

    @BindView(R.id.weather_iv_windmill)
    ImageView mWindMill;

    @BindView(R.id.weather_tv_wind_direction_value)
    TextView mWindDirection;

    @BindView(R.id.weather_tv_wind_value)
    TextView mWind;

    @BindView(R.id.weather_sun)
    SunView mSun;

    private Context mContext;

    private HourlyWeatherAdapter mHourlyWeatherAdapter;

    private DailyWeatherAdapter mDailyWeatherAdapter;

    private String mCity;

    public void setCity(String city) {
        this.mCity = city;
    }

    private City mCityModel;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_weather;
    }

    @Override
    protected void initAllMemberViews(Bundle savedInstanceState) {
        mContext = context.getApplicationContext();

        mSmartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            mPresenter.refreshWeather(mCity);
            mPresenter.refreshAir(mCity);
            mPresenter.refreshSun(mCity);
        });

        mPresenter.autoUpdate(mCity);

    }

    @Override
    protected WeatherPresenter createPresenter() {
        return new WeatherPresenter();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mWindMill != null) {
            mWindMill.clearAnimation();
        }
    }

    @Override
    public void onLoadFailed(Throwable e) {
        Log.i(TAG, "onLoadFailed,e:" + e.getMessage());
        ToastUtils.showShort("加载失败");
    }

    @Override
    public void onRefreshFailed(Throwable e) {
        Log.i(TAG, "onRefreshFailed,e:" + e.getMessage());
        ToastUtils.showShort("刷新失败");
        mSmartRefreshLayout.finishRefresh(true);
    }

    @Override
    public void onAutoUpdate(City city) {
        Log.i(TAG, "onAutoUpdate,city:" + city);
        mCityModel = city;
        long lastUpdateTime = city.getUpdateTime();
        if (lastUpdateTime != 0) {

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(lastUpdateTime);
            mLastUpdate.setText("上次更新时间:" + format.format(date));

            boolean autoUpdate = SPUtils.getInstance().getBoolean("auto_update", true);
            if (autoUpdate) {
                int intervalIndex = SPUtils.getInstance().getInt("interval", 0);
                if ((System.currentTimeMillis() - lastUpdateTime) > ConstantValue.INTERVALS[intervalIndex] * ConstantValue.HOUR_IN_MILLISECOND) {
                    //更新数据
                    mPresenter.refreshWeather(mCity);
                    mPresenter.refreshAir(mCity);
                    mPresenter.refreshSun(mCity);
                    return;
                }
            }

        }

        mPresenter.loadWeather(mCity);
        mPresenter.loadAir(mCity);
        mPresenter.loadSun(mCity);

    }

    @Override
    public void onSetCityModel(City city) {
        mCityModel = city;
    }

    @Override
    public void onLoadWeather(Weather weather) {
        Log.i(TAG, "onLoadWeather,weather:" + weather);
        loadWeather(weather);
    }

    @Override
    public void onRefreshWeather(Weather weather) {
        Log.i(TAG, "onRefreshWeather,weather:" + weather);
        mSmartRefreshLayout.finishRefresh(true);
        loadWeather(weather);
    }

    @Override
    public void onLoadAir(Air air) {
        Log.i(TAG, "onLoadAir,air:" + air);
        loadAir(air);
    }

    @Override
    public void onRefreshAir(Air air) {
        Log.i(TAG, "onRefreshAir,air:" + air);
        loadAir(air);
    }

    @Override
    public void onLoadSun(Sun sun) {
        Log.i(TAG, "onLoadSun,sun:" + sun);
        loadSun(sun);
    }

    @Override
    public void onRefreshSun(Sun sun) {
        Log.i(TAG, "onRefreshSun,sun:" + sun);
        loadSun(sun);
    }

    @TargetApi(Build.VERSION_CODES.N)
    private void loadWeather(Weather weather) {
        mScrollView.fullScroll(ScrollView.FOCUS_UP);

        long lastUpdateTime = mCityModel.getUpdateTime();
        if (lastUpdateTime != 0) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(lastUpdateTime);
            mLastUpdate.setText(mContext.getString(R.string.update_time) + format.format(date));
        }

        Weather.HeWeather6Bean heWeather6Bean = weather.getHeWeather6().get(0);

        mCurrentTemperature.setText(heWeather6Bean.getNow().getTmp() + ConstantValue.TEMPERATURE_UNIT);
        mCurrentWeather.setText(heWeather6Bean.getNow().getCond_txt());
        mTodayTemperature.setText(heWeather6Bean.getDaily_forecast().get(0).getTmp_max() + "/" + heWeather6Bean.getDaily_forecast().get(0).getTmp_min() + ConstantValue.TEMPERATURE_UNIT);
        Weather.HeWeather6Bean.LifestyleBean air = heWeather6Bean.getLifestyle().stream().filter(l -> l.getType().equals(ConstantValue.KEY_AIR)).findFirst().get();
        mTodayAir.setText(mContext.getString(R.string.air) + air.getBrf());

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        mHourlyRecyclerView.setLayoutManager(layoutManager);
        mHourlyWeatherAdapter = new HourlyWeatherAdapter(weather, mContext);
        mHourlyRecyclerView.setAdapter(mHourlyWeatherAdapter);

        mDailyWeatherAdapter = new DailyWeatherAdapter(mContext, weather);
        mDailyListView.setAdapter(mDailyWeatherAdapter);

        mComfortable.setCircleView("", heWeather6Bean.getNow().getHum() + "%", Integer.valueOf(heWeather6Bean.getNow().getHum()));
        mTemp.setText(heWeather6Bean.getNow().getFl());
        mUv.setText(heWeather6Bean.getDaily_forecast().get(0).getUv_index());

        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_wind_mill);
        LinearInterpolator linearInterpolator = new LinearInterpolator();
        animation.setInterpolator(linearInterpolator);
        mWindMill.startAnimation(animation);

        mWindDirection.setText(heWeather6Bean.getNow().getWind_dir());
        mWind.setText(heWeather6Bean.getNow().getWind_sc());

        mLayout.setVisibility(View.VISIBLE);
    }

    private void loadAir(Air air) {
        Air.HeWeather6Bean.AirNowCityBean airNowCity = air.getHeWeather6().get(0).getAir_now_city();
        if (airNowCity == null) {
            return;
        }
        mAir.setCircleView(airNowCity.getQlty(), airNowCity.getAqi(), Integer.valueOf(airNowCity.getAqi()));
        mPm10.setText(airNowCity.getPm10());
        mPm25.setText(airNowCity.getPm25());
        mNo2.setText(airNowCity.getNo2());
        mSo2.setText(airNowCity.getSo2());
        mO3.setText(airNowCity.getO3());
        mCo.setText(airNowCity.getCo());
    }

    private void loadSun(Sun sun) {
        Sun.HeWeather6Bean.SunriseSunsetBean sunriseSunsetBean = sun.getHeWeather6().get(0).getSunrise_sunset().get(0);
        mSun.setSunView(sunriseSunsetBean.getSr(), sunriseSunsetBean.getSs());
    }
}
