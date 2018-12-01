package com.wangyz.weather.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author wangyz
 * 天气的适配器。重写instantiateItem()和getItemPosition()，以解决替换Fragment时不更新的问题。
 */
public class WeatherAdapter extends FragmentPagerAdapter {

    private List<Fragment> mList;

    private FragmentManager mFragmentManager;

    public WeatherAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.mList = list;
        this.mFragmentManager = fm;
    }

    @Override
    public Fragment getItem(int i) {
        return mList.get(i);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        removeFragment(container, position);
        return super.instantiateItem(container, position);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    private void removeFragment(ViewGroup container, int index) {
        String tag = getFragmentTag(container.getId(), index);
        Fragment fragment = mFragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            return;
        }
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();
        mFragmentManager.executePendingTransactions();
    }

    private String getFragmentTag(int viewId, int index) {
        String tag = "";
        try {
            Class<FragmentPagerAdapter> cls = FragmentPagerAdapter.class;
            Class<?>[] parameterTypes = {int.class, long.class};
            Method method = cls.getDeclaredMethod("makeFragmentName", parameterTypes);
            method.setAccessible(true);
            tag = (String) method.invoke(this, viewId, index);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tag;
    }
}
