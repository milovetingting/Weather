package com.wangyz.weather.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wangyz.weather.ConstantValue;
import com.wangyz.weather.R;
import com.wangyz.weather.bean.model.Weather;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author wangyz
 * 逐天展示信息的适配器
 */
public class DailyWeatherAdapter extends BaseAdapter {

    private Context mContext;

    private Weather mWeather;

    public DailyWeatherAdapter(Context mContext, Weather mWeather) {
        this.mContext = mContext;
        this.mWeather = mWeather;
    }

    @Override
    public int getCount() {
        return mWeather.getHeWeather6().get(0).getDaily_forecast().size();
    }

    @Override
    public Object getItem(int position) {
        return mWeather.getHeWeather6().get(0).getDaily_forecast().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_weather_daily, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Weather.HeWeather6Bean.DailyForecastBean dailyForecastBean = mWeather.getHeWeather6().get(0).getDaily_forecast().get(position);
        viewHolder.day.setText(dailyForecastBean.getDate());
        String path = "icon/" + dailyForecastBean.getCond_code_d() + ".png";
        try {
            viewHolder.weather.setImageDrawable(Drawable.createFromStream(mContext.getAssets().open(path), null));
        } catch (IOException e) {
            e.printStackTrace();
        }
        viewHolder.temperature.setText(dailyForecastBean.getTmp_max() + "/" + dailyForecastBean.getTmp_min() + ConstantValue.TEMPERATURE_UNIT);
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.weather_item_tv_day)
        TextView day;

        @BindView(R.id.weather_item_iv_weather)
        ImageView weather;

        @BindView(R.id.weather_item_tv_temperature)
        TextView temperature;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
