package com.wangyz.weather.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.wangyz.weather.ConstantValue;
import com.wangyz.weather.R;
import com.wangyz.weather.adapter.SearchCityAdapter;
import com.wangyz.weather.adapter.TopCityAdapter;
import com.wangyz.weather.base.BaseActivity;
import com.wangyz.weather.bean.model.City;
import com.wangyz.weather.contract.WeatherContract;
import com.wangyz.weather.presenter.CityPresenter;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnTextChanged;

/**
 * @author wangyz
 * CityActivity
 */
public class CityActivity extends BaseActivity<WeatherContract.CityView, CityPresenter> implements WeatherContract.CityView {

    private static final String TAG = CityActivity.class.getSimpleName();

    private Context mContext;

    @BindView(R.id.city_iv_back)
    ImageView mBack;

    @BindView(R.id.city_et_search)
    EditText mSearch;

    @BindView(R.id.city_gv_top)
    GridView mGridView;

    @BindView(R.id.city_lv_search)
    ListView mListView;

    @BindView(R.id.city_tv_title)
    TextView mTitle;

    private SearchCityAdapter mSearchCityAdapter;

    private TopCityAdapter mTopCityAdapter;

    private City mSearchCity;

    private City mTopCity;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_city;
    }

    @Override
    protected void initAllMemberViews(Bundle savedInstanceState) {
        mContext = getApplicationContext();

        //设置Hint字体大小
        SpannableString ss = new SpannableString(mContext.getString(R.string.hint_search));
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(12, true);
        ss.setSpan(ass, 0, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mSearch.setHint(new SpannableString(ss));

        mPresenter.loadTopCity();
        mPresenter.searchCity(mSearch.getText().toString(), mSearch);
    }

    @Override
    protected CityPresenter createPresenter() {
        return new CityPresenter();
    }

    @Override
    public void onLoadTopCityComplete(City city) {
        Log.i(TAG, "onLoadTopCityComplete,city:" + city);
        if (ConstantValue.REQUEST_SUCCESS.equals(city.getHeWeather6().get(0).getStatus())) {
            mTopCity = city;
            mTopCityAdapter = new TopCityAdapter(mContext, mTopCity);
            mGridView.setAdapter(mTopCityAdapter);
        }
    }

    @Override
    public void onSaveCityComplete(com.wangyz.weather.bean.db.City city) {
        Log.i(TAG, "onSaveCityComplete,city:" + city);
        Intent mainIntent = new Intent(mContext, MainActivity.class);
        mainIntent.putExtra("city", city.getName());
        startActivity(mainIntent);
        finish();
    }

    @Override
    public void onLoadFailed(Throwable e) {
        Log.i(TAG, "onLoadFailed,e:" + e.getMessage());
        ToastUtils.showShort(mContext.getString(R.string.load_failed));
    }

    @Override
    public void onSearchCityComplete(City city) {
        Log.i(TAG, "onSearchCityComplete,city:" + city);
        if (ConstantValue.REQUEST_SUCCESS.equals(city.getHeWeather6().get(0).getStatus())) {
            mSearchCity = city;
            mSearchCityAdapter = new SearchCityAdapter(mContext, mSearchCity);
            mListView.setAdapter(mSearchCityAdapter);
            mGridView.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            String result = String.format(mContext.getString(R.string.search_result), city.getHeWeather6().get(0).getBasic().size() + "");
            mTitle.setText(result);
        } else {
            mGridView.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            String result = String.format(mContext.getString(R.string.search_result), "0");
            mTitle.setText(result);
        }
    }

    @OnTextChanged(R.id.city_et_search)
    public void onSearch() {
        if (TextUtils.isEmpty(mSearch.getText().toString())) {
            mGridView.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
            mTitle.setText(mContext.getString(R.string.top_city));
        }
    }

    @OnItemClick(R.id.city_lv_search)
    public void onSearchItemClick(AdapterView<?> parent, View view, int position, long l) {
        City.HeWeather6Bean.BasicBean basicBean = mSearchCity.getHeWeather6().get(0).getBasic().get(position);
        com.wangyz.weather.bean.db.City city = new com.wangyz.weather.bean.db.City();
        city.setName(basicBean.getLocation());
        mPresenter.saveCity(city);
    }

    @OnItemClick(R.id.city_gv_top)
    public void onTopItemClick(AdapterView<?> parent, View view, int position, long l) {
        City.HeWeather6Bean.BasicBean basicBean = mTopCity.getHeWeather6().get(0).getBasic().get(position);
        com.wangyz.weather.bean.db.City city = new com.wangyz.weather.bean.db.City();
        city.setName(basicBean.getLocation());
        mPresenter.saveCity(city);
    }

    @OnClick(R.id.city_iv_back)
    public void back() {
        finish();
    }

}
