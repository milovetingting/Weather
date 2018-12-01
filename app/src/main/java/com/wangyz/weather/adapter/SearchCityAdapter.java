package com.wangyz.weather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wangyz.weather.R;
import com.wangyz.weather.bean.model.City;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author wangyz
 * 搜索城市的适配器
 */
public class SearchCityAdapter extends BaseAdapter {

    private Context mContext;

    private City mCity;

    public SearchCityAdapter(Context mContext, City mCity) {
        this.mContext = mContext;
        this.mCity = mCity;
    }

    @Override
    public int getCount() {
        return mCity.getHeWeather6().get(0).getBasic().size();
    }

    @Override
    public Object getItem(int position) {
        return mCity.getHeWeather6().get(0).getBasic().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_city_search, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        City.HeWeather6Bean.BasicBean basicBean = mCity.getHeWeather6().get(0).getBasic().get(position);
        viewHolder.mSearchInfo.setText(basicBean.getLocation() + "," + basicBean.getParent_city() + "," + basicBean.getAdmin_area() + "," + basicBean.getCnty());
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.city_item_tv_search_info)
        TextView mSearchInfo;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
