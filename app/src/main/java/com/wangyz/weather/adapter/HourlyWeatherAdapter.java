package com.wangyz.weather.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wangyz.weather.ConstantValue;
import com.wangyz.weather.R;
import com.wangyz.weather.bean.model.Weather;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wangyz
 * 逐小时天气信息的适配器
 */
public class HourlyWeatherAdapter extends RecyclerView.Adapter<HourlyWeatherAdapter.ViewHolder> {

    private Context mContext;

    private Weather mWeather;

    public HourlyWeatherAdapter(Weather mWeather, Context mContext) {
        this.mContext = mContext;
        this.mWeather = mWeather;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_weather_hourly, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Weather.HeWeather6Bean.HourlyBean hourlyBean = mWeather.getHeWeather6().get(0).getHourly().get(i);
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = format.parse(hourlyBean.getTime());
            format = new SimpleDateFormat("HH:mm");
            viewHolder.time.setText(format.format(date));
            String path = "icon/" + hourlyBean.getCond_code() + ".png";
            viewHolder.icon.setImageDrawable(Drawable.createFromStream(mContext.getAssets().open(path), null));
        } catch (Exception e) {
            e.printStackTrace();
        }
        viewHolder.temperature.setText(hourlyBean.getTmp() + ConstantValue.TEMPERATURE_UNIT);
    }

    @Override
    public int getItemCount() {
        return mWeather.getHeWeather6().get(0).getHourly().size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView time;

        public ImageView icon;

        public TextView temperature;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.weather_item_tv_hourly_time);
            icon = itemView.findViewById(R.id.weather_item_iv_hourly_icon);
            temperature = itemView.findViewById(R.id.weather_item_tv_hourly_temperature);
        }
    }
}
