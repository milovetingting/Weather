package com.wangyz.weather.bean.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author wangyz
 * 日出日落信息
 */
public class Sun implements Serializable {

    private List<HeWeather6Bean> HeWeather6;

    public List<HeWeather6Bean> getHeWeather6() {
        return HeWeather6;
    }

    public void setHeWeather6(List<HeWeather6Bean> HeWeather6) {
        this.HeWeather6 = HeWeather6;
    }

    public static class HeWeather6Bean implements Serializable {
        /**
         * basic : {"cid":"CN101280601","location":"深圳","parent_city":"深圳","admin_area":"广东","cnty":"中国","lat":"22.54700089","lon":"114.08594513","tz":"+8.00"}
         * update : {"loc":"2018-11-16 14:45","utc":"2018-11-16 06:45"}
         * status : ok
         * sunrise_sunset : [{"date":"2018-11-16","mr":"13:20","ms":"00:04","sr":"06:37","ss":"17:39"},{"date":"2018-11-17","mr":"13:57","ms":"00:55","sr":"06:38","ss":"17:39"},{"date":"2018-11-18","mr":"14:33","ms":"01:46","sr":"06:38","ss":"17:38"},{"date":"2018-11-19","mr":"15:10","ms":"02:38","sr":"06:39","ss":"17:38"},{"date":"2018-11-20","mr":"15:47","ms":"03:31","sr":"06:40","ss":"17:38"},{"date":"2018-11-21","mr":"16:25","ms":"04:25","sr":"06:40","ss":"17:38"},{"date":"2018-11-22","mr":"17:08","ms":"05:23","sr":"06:41","ss":"17:38"}]
         */

        private BasicBean basic;
        private UpdateBean update;
        private String status;
        private List<SunriseSunsetBean> sunrise_sunset;

        public BasicBean getBasic() {
            return basic;
        }

        public void setBasic(BasicBean basic) {
            this.basic = basic;
        }

        public UpdateBean getUpdate() {
            return update;
        }

        public void setUpdate(UpdateBean update) {
            this.update = update;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List<SunriseSunsetBean> getSunrise_sunset() {
            return sunrise_sunset;
        }

        public void setSunrise_sunset(List<SunriseSunsetBean> sunrise_sunset) {
            this.sunrise_sunset = sunrise_sunset;
        }

        public static class BasicBean implements Serializable {
            /**
             * cid : CN101280601
             * location : 深圳
             * parent_city : 深圳
             * admin_area : 广东
             * cnty : 中国
             * lat : 22.54700089
             * lon : 114.08594513
             * tz : +8.00
             */

            private String cid;
            private String location;
            private String parent_city;
            private String admin_area;
            private String cnty;
            private String lat;
            private String lon;
            private String tz;

            public String getCid() {
                return cid;
            }

            public void setCid(String cid) {
                this.cid = cid;
            }

            public String getLocation() {
                return location;
            }

            public void setLocation(String location) {
                this.location = location;
            }

            public String getParent_city() {
                return parent_city;
            }

            public void setParent_city(String parent_city) {
                this.parent_city = parent_city;
            }

            public String getAdmin_area() {
                return admin_area;
            }

            public void setAdmin_area(String admin_area) {
                this.admin_area = admin_area;
            }

            public String getCnty() {
                return cnty;
            }

            public void setCnty(String cnty) {
                this.cnty = cnty;
            }

            public String getLat() {
                return lat;
            }

            public void setLat(String lat) {
                this.lat = lat;
            }

            public String getLon() {
                return lon;
            }

            public void setLon(String lon) {
                this.lon = lon;
            }

            public String getTz() {
                return tz;
            }

            public void setTz(String tz) {
                this.tz = tz;
            }

            @Override
            public String toString() {
                return "BasicBean{" +
                        "cid='" + cid + '\'' +
                        ", location='" + location + '\'' +
                        ", parent_city='" + parent_city + '\'' +
                        ", admin_area='" + admin_area + '\'' +
                        ", cnty='" + cnty + '\'' +
                        ", lat='" + lat + '\'' +
                        ", lon='" + lon + '\'' +
                        ", tz='" + tz + '\'' +
                        '}';
            }
        }

        public static class UpdateBean implements Serializable {
            /**
             * loc : 2018-11-16 14:45
             * utc : 2018-11-16 06:45
             */

            private String loc;
            private String utc;

            public String getLoc() {
                return loc;
            }

            public void setLoc(String loc) {
                this.loc = loc;
            }

            public String getUtc() {
                return utc;
            }

            public void setUtc(String utc) {
                this.utc = utc;
            }

            @Override
            public String toString() {
                return "UpdateBean{" +
                        "loc='" + loc + '\'' +
                        ", utc='" + utc + '\'' +
                        '}';
            }
        }

        public static class SunriseSunsetBean implements Serializable {
            /**
             * date : 2018-11-16
             * mr : 13:20
             * ms : 00:04
             * sr : 06:37
             * ss : 17:39
             */

            private String date;
            private String mr;
            private String ms;
            private String sr;
            private String ss;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getMr() {
                return mr;
            }

            public void setMr(String mr) {
                this.mr = mr;
            }

            public String getMs() {
                return ms;
            }

            public void setMs(String ms) {
                this.ms = ms;
            }

            public String getSr() {
                return sr;
            }

            public void setSr(String sr) {
                this.sr = sr;
            }

            public String getSs() {
                return ss;
            }

            public void setSs(String ss) {
                this.ss = ss;
            }

            @Override
            public String toString() {
                return "SunriseSunsetBean{" +
                        "date='" + date + '\'' +
                        ", mr='" + mr + '\'' +
                        ", ms='" + ms + '\'' +
                        ", sr='" + sr + '\'' +
                        ", ss='" + ss + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "HeWeather6Bean{" +
                    "basic=" + basic +
                    ", update=" + update +
                    ", status='" + status + '\'' +
                    ", sunrise_sunset=" + sunrise_sunset +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Sun{" +
                "HeWeather6=" + HeWeather6 +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Sun) {
            if (obj.toString().equals(this.toString())) {
                return true;
            }
        }
        return super.equals(obj);
    }
}
