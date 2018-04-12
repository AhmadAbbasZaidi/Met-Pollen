package com.cfp.metpollen.data.db.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.cfp.metpollen.view.utilities.utils;

/**
 * Created by AhmedAbbas on 12/19/2017.
 */
@Table(name = "Forecast")
public class ForecastModel extends Model {


    @Column(name = "Min_Temp")
    private String minTemperature;
    @Column(name = "Temp")
    private String temperature;
    @Column(name = "Entry_Time")
    private String entryTime;
    @Column(name = "Wind_Speed")
    private String windSpeed;
    @Column(name = "Formated_Weather_Time")
    private String formattedWeatherTime;
    @Column(name = "Formated_Weather_Date")
    private String formattedWeatherDate;
    @Column(name = "Formated_Hour")
    private String formattedHour;
    @Column(name = "Formated_Min")
    private String formattedMin;
    @Column(name = "Formated_Day")
    private String formattedDay;
    @Column(name = "Formated_Mon")
    private String formattedMon;
    @Column(name = "Formated_Year")
    private String formattedYear;
    @Column(name = "Max_Temp")
    private String maxTemperature;
    @Column(name = "Precipitation")
    private String precipitation;
    @Column(name = "Weather_Time")
    private String weatherTime;
    @Column(name = "Pressure_Mean_Sea_Level")
    private String pressureMeanSeaLevel;
    @Column(name = "Relative_Humidity")
    private String relativeHumidity;
    @Column(name = "Wind_Direction")
    private String windDirection;
    @Column(name = "Cloud")
    private String cloud;
    @Column(name = "Forecast_Id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private String forecastId;
    @Column(name = "Grid_Location")
    private String gridLocationId;
    @Column(name = "Sun_Rise_Hour")
    private String sunRiseHour;
    @Column(name = "Sun_Rise_Min")
    private String sunRiseMin;
    @Column(name = "image")
    private String imageUrl;
    @Column(name = "Sun_Set_Hour")
    private String sunSetHour;
    @Column(name = "Sun_Set_Min")
    private String sunSetMin;

    public ForecastModel() {
        super();

        this.minTemperature = "--";
        this.temperature = "--";
        this.entryTime = "--";
        this.windSpeed = "--";
        this.formattedWeatherTime = "--";
        this.formattedWeatherDate = "--";
        this.maxTemperature = "--";
        this.precipitation = "--";
        this.weatherTime = "--";
        this.pressureMeanSeaLevel = "--";
        this.relativeHumidity = "--";
        this.windDirection = "--";
        this.cloud = "--";
        this.forecastId = "--";
        this.gridLocationId = "--";
        this.imageUrl = "--";
        this.setSunRiseHour("--");
        this.setSunRiseMin("--");
        this.setSunSetHour("--");
        this.setSunSetMin("--");
        setFormattedHour("--");
        setFormattedMin("--");
        setFormattedDay("--");
        setFormattedMon("--");
        setFormattedYear("--");
    }

    public ForecastModel(String minTemperature, String temperature, String entryTime, String windSpeed, String formattedWeatherTime, String formattedWeatherDate, String maxTemperature, String precipitation, String weatherTime, String pressureMeanSeaLevel, String relativeHumidity, String windDirection, String cloud, String forecastId, String gridLocationId, String imageUrl) {
        this.minTemperature = minTemperature;
        this.temperature = temperature;
        this.entryTime = entryTime;
        this.windSpeed = windSpeed;
        this.formattedWeatherTime = formattedWeatherTime;
        this.formattedWeatherDate = formattedWeatherDate;
        this.maxTemperature = maxTemperature;
        this.precipitation = precipitation;
        this.weatherTime = weatherTime;
        this.pressureMeanSeaLevel = pressureMeanSeaLevel;
        this.relativeHumidity = relativeHumidity;
        this.windDirection = windDirection;
        this.cloud = cloud;
        this.forecastId = forecastId;
        this.gridLocationId = gridLocationId;
        this.imageUrl = imageUrl;
        this.setSunRiseHour("00");
        this.setSunRiseMin("00");
        this.setSunSetHour("00");
        this.setSunSetMin("00");
        setFormattedHour(utils.getRequiredTimeField(utils.HOUR, formattedWeatherTime));
        setFormattedMin(utils.getRequiredTimeField(utils.MIN, formattedWeatherTime));
        setFormattedDay(utils.getRequiredTimeField(utils.DAY, formattedWeatherTime));
        setFormattedMon(utils.getRequiredTimeField(utils.MONTH, formattedWeatherTime));
        setFormattedYear(utils.getRequiredTimeField(utils.YEAR, formattedWeatherTime));
    }

    public String getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(String minTemperature) {
        this.minTemperature = minTemperature;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(String maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public String getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(String precipitation) {
        this.precipitation = precipitation;
    }

    public String getPressureMeanSeaLevel() {
        return pressureMeanSeaLevel;
    }

    public void setPressureMeanSeaLevel(String pressureMeanSeaLevel) {
        this.pressureMeanSeaLevel = pressureMeanSeaLevel;
    }

    public String getRelativeHumidity() {
        return relativeHumidity;
    }

    public void setRelativeHumidity(String relativeHumidity) {
        this.relativeHumidity = relativeHumidity;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public String getCloud() {
        return cloud;
    }

    public void setCloud(String cloud) {
        this.cloud = cloud;
    }

    public String getForecastId() {
        return forecastId;
    }

    public void setForecastId(String forecastId) {
        this.forecastId = forecastId;
    }

    public String getGridLocationId() {
        return gridLocationId;
    }

    public void setGridLocationId(String gridLocationId) {
        this.gridLocationId = gridLocationId;
    }

    public String getFormattedWeatherDate() {
        return formattedWeatherDate;
    }

    public void setFormattedWeatherDate(String formattedWeatherDate) {
        this.formattedWeatherDate = formattedWeatherDate;
    }

    public void setDefault() {
        this.setSunRiseHour("--");
        this.setSunRiseMin("--");
        this.setSunSetHour("--");
        this.setSunSetMin("--");

    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public String getWeatherTime() {
        return weatherTime;
    }

    public void setWeatherTime(String weatherTime) {
        this.weatherTime = weatherTime;
    }

}
