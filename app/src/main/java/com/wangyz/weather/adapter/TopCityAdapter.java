package com.wangyz.weather.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wangyz.weather.ConstantValue;
import com.wangyz.weather.R;
import com.wangyz.weather.bean.model.City;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author wangyz
 * 热门城市的适配器
 */
public class TopCityAdapter extends BaseAdapter {

    private Context mContext;

    private City mCity;

    private int mScreenWidth;

    public TopCityAdapter(Context mContext, City mCity) {
        this.mContext = mContext;
        this.mCity = mCity;
        mScreenWidth = getScreenWidth(mContext);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_city_top, null);
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(mScreenWidth / 3 - 2, 120);
            convertView.setLayoutParams(params);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        City.HeWeather6Bean.BasicBean basicBean = mCity.getHeWeather6().get(0).getBasic().get(position);
        int itemViewType = getItemViewType(position);
        if (itemViewType == 0) {
            //不显示国家
            viewHolder.mName.setVisibility(View.VISIBLE);
            viewHolder.mName2.setVisibility(View.GONE);
            viewHolder.mCountry.setVisibility(View.GONE);
            viewHolder.mName.setText(basicBean.getLocation());
        } else if (itemViewType == 1) {
            //显示国家
            viewHolder.mName.setVisibility(View.GONE);
            viewHolder.mName2.setVisibility(View.VISIBLE);
            viewHolder.mCountry.setVisibility(View.VISIBLE);
            viewHolder.mName2.setText(basicBean.getLocation());
            viewHolder.mCountry.setText(basicBean.getCnty());
        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        int itemViewType = 0;
        City.HeWeather6Bean.BasicBean basicBean = mCity.getHeWeather6().get(0).getBasic().get(position);
        if (ConstantValue.COUNTRY_CN.equals(basicBean.getCnty())) {
            itemViewType = 0;
        } else {
            itemViewType = 1;
        }
        return itemViewType;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    static class ViewHolder {
        @BindView(R.id.city_item_tv_top_name)
        TextView mName;

        @BindView(R.id.city_item_tv_top_name2)
        TextView mName2;

        @BindView(R.id.city_item_tv_top_country)
        TextView mCountry;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    private int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }
}
