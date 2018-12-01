package com.wangyz.weather.bean.db;

import org.litepal.crud.LitePalSupport;

/**
 * 选择的城市
 *
 * @author wangyz
 */
public class City extends LitePalSupport {

    private String name;

    private long updateTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "City{" +
                "name='" + name + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof City) {
            if (obj.toString().equals(this.toString())) {
                return true;
            }
        }
        return super.equals(obj);
    }
}
