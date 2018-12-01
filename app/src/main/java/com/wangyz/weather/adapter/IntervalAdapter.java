package com.wangyz.weather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.wangyz.weather.ConstantValue;
import com.wangyz.weather.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author wangyz
 * 更新间隔适配器
 */
public class IntervalAdapter extends BaseAdapter {

    private Context mContext;

    private int intervalIndex;

    private String[] items = {"1小时", "2小时", "6小时", "12小时", "24小时"};

    public IntervalAdapter(Context mContext) {
        this.mContext = mContext;
        intervalIndex = SPUtils.getInstance().getInt("interval", 0);
    }

    @Override
    public int getCount() {
        return ConstantValue.INTERVALS.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_weather_setting_interval, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.title.setText(items[position]);
        viewHolder.icon.setSelected(position == intervalIndex);

        return convertView;
    }

    static class ViewHolder {

        @BindView(R.id.weather_setting_interval)
        TextView title;

        @BindView(R.id.weather_setting_interval_icon)
        ImageView icon;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
