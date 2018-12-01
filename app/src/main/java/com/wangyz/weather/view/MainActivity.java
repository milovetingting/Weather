package com.wangyz.weather.view;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.mobeta.android.dslv.DragSortListView;
import com.wangyz.weather.ConstantValue;
import com.wangyz.weather.R;
import com.wangyz.weather.adapter.CityManageAdapter;
import com.wangyz.weather.adapter.IntervalAdapter;
import com.wangyz.weather.adapter.SettingAdapter;
import com.wangyz.weather.adapter.WeatherAdapter;
import com.wangyz.weather.base.BaseActivity;
import com.wangyz.weather.bean.db.City;
import com.wangyz.weather.bean.event.DataEvent;
import com.wangyz.weather.contract.WeatherContract;
import com.wangyz.weather.presenter.MainPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnPageChange;

/**
 * @author wangyz
 * WeatherActivity
 */
public class MainActivity extends BaseActivity<WeatherContract.MainView, MainPresenter> implements WeatherContract.MainView {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.weather_drawer)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.main_iv_city)
    ImageView mManageCity;

    @BindView(R.id.main_tv_city)
    TextView mCity;

    @BindView(R.id.main_iv_setting)
    ImageView mSetting;

    @BindView(R.id.weather_rg)
    RadioGroup mRadioGroup;

    @BindView(R.id.main_viewpager)
    ViewPager mViewPager;

    @BindView(R.id.weather_setting_back)
    ImageView mBack;

    @BindView(R.id.weather_setting_lv)
    ListView mSettingListView;

    ListView mSettingIntervalListView;

    @BindView(R.id.weather_manage_back)
    ImageView mManageBack;

    @BindView(R.id.weather_manage_lv)
    DragSortListView mDragSortListView;

    @BindView(R.id.weather_manage_add)
    ImageView mAddCity;

    private FragmentManager mFragmentManager;
    private WeatherAdapter mAdapter;
    private List<Fragment> mFragments = new ArrayList<>();
    private List<City> mCityList = new ArrayList<>();

    private SettingAdapter mSettingAdapter;

    private IntervalAdapter mIntervalAdapter;

    private Context mContext;

    private String city;

    private CityManageAdapter mCityManageAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initAllMemberViews(Bundle savedInstanceState) {
        mContext = getApplicationContext();

        mFragmentManager = getSupportFragmentManager();

        mAdapter = new WeatherAdapter(mFragmentManager, mFragments);
        mViewPager.setAdapter(mAdapter);

        mSettingAdapter = new SettingAdapter(mContext);
        mSettingListView.setAdapter(mSettingAdapter);

        mCityManageAdapter = new CityManageAdapter(mContext, mCityList);
        mDragSortListView.setDropListener(mDropListener);
        mDragSortListView.setDragEnabled(true);
        mDragSortListView.setAdapter(mCityManageAdapter);

        mPresenter.loadCity();
    }

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onStart() {
        super.onStart();
        if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
            mDrawerLayout.closeDrawer(Gravity.START);
        }
        city = getIntent().getStringExtra(ConstantValue.KEY_CITY);
        if (!TextUtils.isEmpty(city)) {
            mCity.setText(city);
            mPresenter.showCity(city);
        } else {
            mCity.setText("");
        }
    }

    @Override
    public void onLoadCity(List<City> cityList) {
        Log.i(TAG, "onLoadCity,cityList:" + cityList);
        mCityList = cityList;
        if (cityList != null && cityList.size() > 0) {
            mCityManageAdapter.update(mCityList);
            mCity.setText(cityList.get(0).getName());
            loadFragment(mCityList);
        } else {
            loadFragment(mCityList);
            Intent cityIntent = new Intent(mContext, CityActivity.class);
            startActivity(cityIntent);
        }
    }

    @Override
    public void onShowCity(City model) {
        if (model != null) {
            int position = mCityList.indexOf(model);
            if (position == -1) {
                mCityList.add(model);
                addFragment(city);
                mViewPager.setVisibility(View.VISIBLE);
                mViewPager.setCurrentItem(mCityList.size() - 1);
                mCityManageAdapter.update(mCityList);
                setRadioButtonEnabled(mCityList.size() - 1);
            } else {
                mViewPager.setCurrentItem(position);
                setRadioButtonEnabled(position);
            }
        }
        city = null;
    }

    @Override
    public void onSaveCityList(List<City> list) {
        onLoadCity(list);
    }

    private void loadFragment(List<City> cityList) {
        mFragments.clear();
        mRadioGroup.removeAllViews();
        for (City city : cityList) {
            WeatherFragment weatherFragment = new WeatherFragment();
            weatherFragment.setCity(city.getName());
            mFragments.add(weatherFragment);
            addRadioButton();
        }
        mAdapter.notifyDataSetChanged();
        if (mFragments.size() > 0) {
            mViewPager.setVisibility(View.VISIBLE);
            mViewPager.setCurrentItem(0);
            setRadioButtonEnabled(0);
        } else {
            mViewPager.setVisibility(View.GONE);
        }
    }

    private void addFragment(String city) {
        WeatherFragment weatherFragment = new WeatherFragment();
        weatherFragment.setCity(city);
        mFragments.add(weatherFragment);
        mAdapter.notifyDataSetChanged();
        addRadioButton();
    }

    private void addRadioButton() {
        RadioButton radioButton = new RadioButton(this);
        radioButton.setEnabled(false);
        radioButton.setBackgroundResource(R.drawable.radio_selector);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(10, 10);
        params.leftMargin = 5;
        params.rightMargin = 5;
        mRadioGroup.addView(radioButton, params);
    }

    private void setRadioButtonEnabled(int position) {
        for (int i = 0; i < mCityList.size(); i++) {
            mRadioGroup.getChildAt(i).setEnabled(false);
        }
        mRadioGroup.getChildAt(position).setEnabled(true);
    }

    @OnClick(R.id.main_iv_city)
    public void manageCity() {
        mDrawerLayout.openDrawer(Gravity.START);
    }

    @OnClick(R.id.main_iv_setting)
    public void setting() {
        mDrawerLayout.openDrawer(Gravity.END);
    }

    @OnPageChange(R.id.main_viewpager)
    public void onPageChanged(int position) {
        try {
            String city = mCityList.get(position).getName();
            mCity.setText(city);
            setRadioButtonEnabled(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.weather_setting_back)
    public void closeSettingDrawer() {
        mDrawerLayout.closeDrawer(Gravity.END);
    }

    @OnClick(R.id.weather_manage_back)
    public void closeManageDrawer() {
        mDrawerLayout.closeDrawer(Gravity.START);
    }

    @OnClick(R.id.weather_manage_add)
    public void addCity() {
        Intent cityIntent = new Intent(mContext, CityActivity.class);
        startActivity(cityIntent);
    }

    @OnItemClick(R.id.weather_setting_lv)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            SettingAdapter.ViewHolder viewHolder = (SettingAdapter.ViewHolder) view.getTag();
            viewHolder.control.setChecked(!viewHolder.control.isChecked());
            SPUtils.getInstance().put(ConstantValue.KEY_AUTO_UPDATE, viewHolder.control.isChecked());
            mSettingAdapter.notifyDataSetChanged();
        } else if (position == 1) {
            showDialog();
        }
    }

    private void showDialog() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_setting_interval, null);
        mSettingIntervalListView = view.findViewById(R.id.weather_dialog_setting_lv);
        mIntervalAdapter = new IntervalAdapter(mContext);
        mSettingIntervalListView.setAdapter(mIntervalAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        mSettingIntervalListView.setOnItemClickListener((parent, view1, position, id) -> {
            SPUtils.getInstance().put(ConstantValue.KEY_INTERVAL, position);
            alertDialog.dismiss();
            mSettingAdapter.notifyDataSetChanged();
        });

    }

    @Override
    public boolean onKeyDown(int i, KeyEvent keyevent) {
        if (keyevent != null && keyevent.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (mDrawerLayout.isDrawerOpen(Gravity.END)) {
                mDrawerLayout.closeDrawer(Gravity.END);
                return true;
            } else if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
                mDrawerLayout.closeDrawer(Gravity.START);
                return true;
            }
        }
        return super.onKeyDown(i, keyevent);
    }

    private DragSortListView.DropListener mDropListener = new DragSortListView.DropListener() {

        @Override
        public void drop(int from, int to) {
            if (from != to) {
                City item = (City) mCityManageAdapter.getItem(from);
                mCityManageAdapter.remove(from);
                mCityManageAdapter.insert(item, to);
                mCityList = mCityManageAdapter.getList();
                mPresenter.saveCityList(mCityList);
            }
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataChangeEvent(DataEvent event) {
        mCityList = mCityManageAdapter.getList();
        mPresenter.saveCityList(mCityList);
    }

}
