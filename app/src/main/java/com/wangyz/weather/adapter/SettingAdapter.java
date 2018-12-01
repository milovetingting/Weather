package com.wangyz.weather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.wangyz.weather.ConstantValue;
import com.wangyz.weather.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author wangyz
 * 设置的适配器
 */
public class SettingAdapter extends BaseAdapter {

    private Context mContext;

    public SettingAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return 2;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_weather_setting, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        boolean control = SPUtils.getInstance().getBoolean(ConstantValue.KEY_AUTO_UPDATE, true);
        int intervalIndex = SPUtils.getInstance().getInt(ConstantValue.KEY_INTERVAL, 0);

        if (position == 0) {
            viewHolder.title.setText(mContext.getString(R.string.auto_update));
            viewHolder.control.setVisibility(View.VISIBLE);
            viewHolder.control.setChecked(control);
            viewHolder.interval.setVisibility(View.GONE);
        } else if (position == 1) {
            viewHolder.title.setText(mContext.getString(R.string.update_interval));
            viewHolder.title.setEnabled(control);
            viewHolder.control.setVisibility(View.GONE);
            viewHolder.interval.setVisibility(View.VISIBLE);
            viewHolder.interval.setText(ConstantValue.INTERVALS[intervalIndex] + mContext.getString(R.string.unit));
            viewHolder.interval.setEnabled(control);
        }

        return convertView;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        if (position == 1) {
            return SPUtils.getInstance().getBoolean(ConstantValue.KEY_AUTO_UPDATE, true);
        }
        return super.isEnabled(position);
    }

    public static class ViewHolder {
        @BindView(R.id.weather_setting_auto_update)
        public TextView title;
        @BindView(R.id.weather_setting_update_switch)
        public Switch control;
        @BindView(R.id.weather_setting_update_interval)
        public TextView interval;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
