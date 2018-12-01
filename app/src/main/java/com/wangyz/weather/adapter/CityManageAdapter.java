package com.wangyz.weather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wangyz.weather.R;
import com.wangyz.weather.bean.db.City;
import com.wangyz.weather.bean.event.DataEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author wangyz
 * 城市管理适配器
 */
public class CityManageAdapter extends BaseAdapter {

    private Context mContext;

    private List<City> mList;

    public CityManageAdapter(Context mContext, List<City> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    public void update(List<City> mList) {
        if (this.mList != null) {
            this.mList.clear();
            this.mList.addAll(mList);
            notifyDataSetChanged();
        }
    }

    public List<City> getList() {
        return mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_weather_manage, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        City city = mList.get(position);
        viewHolder.city.setText(city.getName());

        viewHolder.remove.setOnClickListener(v -> {
            remove(position);
            DataEvent event = new DataEvent();
            EventBus.getDefault().post(event);
        });

        return convertView;
    }

    public void remove(int position) {
        mList.remove(position);
        notifyDataSetChanged();
    }

    public void insert(City city, int position) {
        mList.add(position, city);
        notifyDataSetChanged();
    }

    static class ViewHolder {

        @BindView(R.id.drag_handle)
        ImageView drag;

        @BindView(R.id.weather_manage_item_city)
        TextView city;

        @BindView(R.id.click_remove)
        ImageView remove;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
