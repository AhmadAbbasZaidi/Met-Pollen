package com.cfp.metpollen.view.objectModels;

import com.cfp.metpollen.view.utilities.utils;

/**
 * Created by AhmedAbbas on 1/10/2018.
 */

public class SunRiseSetModel {
    private String formattedTime;
    private String hour;
    private String min;
    private String day;
    private String month;
    private String year;

    private String riseTime;
    private String setTime;
    private String riseHour;
    private String riseMin;
    private String setHour;

    public String getFormattedTime() {
        return formattedTime;
    }

    public void setFormattedTime(String formattedTime) {
        this.formattedTime = formattedTime;

        setHour(utils.getRequiredTimeField(utils.HOUR, formattedTime));
        setMin(utils.getRequiredTimeField(utils.MIN, formattedTime));
        setDay(utils.getRequiredTimeField(utils.DAY, formattedTime));
        setMonth(utils.getRequiredTimeField(utils.MONTH, formattedTime));
        setYear(utils.getRequiredTimeField(utils.YEAR, formattedTime));

    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRiseTime() {
        return riseTime;
    }

    public void setRiseTime(String riseTime) {
        this.riseTime = riseTime;
        setRiseHour(utils.getRequiredTimeField(utils.HOUR, riseTime));
        setRiseMin(utils.getRequiredTimeField(utils.MIN, riseTime));
    }

    public String getSetTime() {
        return setTime;
    }

    public void setSetTime(String setTime) {
        this.setTime = setTime;
        setSetHour(utils.getRequiredTimeField(utils.HOUR, setTime));
        setSetMin(utils.getRequiredTimeField(utils.MIN, setTime));
    }

    public String getRiseHour() {
        return riseHour;
    }

    public void setRiseHour(String riseHour) {
        this.riseHour = riseHour;
    }

    public String getRiseMin() {
        return riseMin;
    }

    public void setRiseMin(String riseMin) {
        this.riseMin = riseMin;
    }

    public String getSetHour() {
        return setHour;
    }

    public void setSetHour(String setHour) {
        this.setHour = setHour;
    }

    public String getSetMin() {
        return setMin;
    }

    public void setSetMin(String setMin) {
        this.setMin = setMin;
    }

    private String setMin;


}
