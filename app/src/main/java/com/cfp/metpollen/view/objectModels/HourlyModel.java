package com.cfp.metpollen.view.objectModels;

import com.cfp.metpollen.view.utilities.utils;

/**
 * Created by AhmedAbbas on 12/20/2017.
 */

public class HourlyModel {
    private String temperature;
    private String formattedWeatherTime;
    private String formattedWeatherDate;
    private String formattedHour;
    private String formattedMin;
    private String formattedDay;
    private String formattedMon;
    private String formattedYear;
    private String precipitation;
    private String cloud;
    private boolean isHeader;
    private String sunRiseHour;
    private String sunRiseMin;
    private String sunSetHour;
    private String sunSetMin;

    public HourlyModel(String temperature, String formattedWeatherTime, String precipitation, String cloud, String sunRiseHour, String sunRiseMin, String sunSetHour, String sunSetMin, boolean isHeader) {
        this.temperature = temperature;
        this.formattedWeatherTime = formattedWeatherTime;
        this.precipitation = precipitation;
        this.cloud = cloud;
        this.isHeader = isHeader;
        setFormattedWeatherDate(utils.getConvertedDateFromOneFormatToOther(formattedWeatherTime, utils.FORMAT1, utils.FORMAT3));
        setFormattedHour(utils.getRequiredTimeField(utils.HOUR, formattedWeatherTime));
        setFormattedMin(utils.getRequiredTimeField(utils.MIN, formattedWeatherTime));
        setFormattedDay(utils.getRequiredTimeField(utils.DAY, formattedWeatherTime));
        setFormattedMon(utils.getRequiredTimeField(utils.MONTH, formattedWeatherTime));
        setFormattedYear(utils.getRequiredTimeField(utils.YEAR, formattedWeatherTime));
        this.sunRiseHour = sunRiseHour;
        this.sunRiseMin = sunRiseMin;
        this.sunSetHour = sunSetHour;
        this.sunSetMin = sunSetMin;
    }

    public String getSunRiseHour() {
        return sunRiseHour;
    }

    public void setSunRiseHour(String sunRiseHour) {
        this.sunRiseHour = sunRiseHour;
    }

    public String getSunRiseMin() {
        return sunRiseMin;
    }

    public void setSunRiseMin(String sunRiseMin) {
        this.sunRiseMin = sunRiseMin;
    }

    public String getSunSetHour() {
        return sunSetHour;
    }

    public void setSunSetHour(String sunSetHour) {
        this.sunSetHour = sunSetHour;
    }

    public String getSunSetMin() {
        return sunSetMin;
    }

    public void setSunSetMin(String sunSetMin) {
        this.sunSetMin = sunSetMin;
    }

    public String getFormattedWeatherDate() {
        return formattedWeatherDate;
    }

    public void setFormattedWeatherDate(String formattedWeatherDate) {
        this.formattedWeatherDate = formattedWeatherDate;
    }

    public String getFormattedHour() {
        return formattedHour;
    }

    public void setFormattedHour(String formattedHour) {
        this.formattedHour = formattedHour;
    }

    public String getFormattedMin() {
        return formattedMin;
    }

    public void setFormattedMin(String formattedMin) {
        this.formattedMin = formattedMin;
    }

    public String getFormattedDay() {
        return formattedDay;
    }

    public void setFormattedDay(String formattedDay) {
        this.formattedDay = formattedDay;
    }

    public String getFormattedMon() {
        return formattedMon;
    }

    public void setFormattedMon(String formattedMon) {
        this.formattedMon = formattedMon;
    }

    public String getFormattedYear() {
        return formattedYear;
    }

    public void setFormattedYear(String formattedYear) {
        this.formattedYear = formattedYear;
    }

    public String getCloud() {
        return cloud;
    }

    public void setCloud(String cloud) {
        this.cloud = cloud;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getFormattedWeatherTime() {
        return formattedWeatherTime;
    }

    public void setFormattedWeatherTime(String formattedWeatherTime) {
        this.formattedWeatherTime = formattedWeatherTime;
        setFormattedWeatherDate(utils.getConvertedDateFromOneFormatToOther(formattedWeatherTime, utils.FORMAT1, utils.FORMAT3));
        setFormattedHour(utils.getRequiredTimeField(utils.HOUR, formattedWeatherTime));
        setFormattedMin(utils.getRequiredTimeField(utils.MIN, formattedWeatherTime));
        setFormattedDay(utils.getRequiredTimeField(utils.DAY, formattedWeatherTime));
        setFormattedMon(utils.getRequiredTimeField(utils.MONTH, formattedWeatherTime));
        setFormattedYear(utils.getRequiredTimeField(utils.YEAR, formattedWeatherTime));
    }

    public String getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(String precipitation) {
        this.precipitation = precipitation;
    }
}
