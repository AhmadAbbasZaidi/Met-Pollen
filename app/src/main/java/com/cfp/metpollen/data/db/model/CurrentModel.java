package com.cfp.metpollen.data.db.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.cfp.metpollen.data.db.DbUtils;
import com.cfp.metpollen.view.utilities.utils;

import java.util.List;

/**
 * Created by AhmedAbbas on 1/19/2018.
 */
@Table(name = "Current")
public class CurrentModel extends Model {

    public static final String NOON = "12";
    public static final String EVENING = "18";
    public static final String TONIGHT = "21";

    @Column(name = "Min_Temp")
    private String minTemperature;

    @Column(name = "Precipitation")
    private String precipitation;

    @Column(name = "Temp")
    private String temperature;

    @Column(name = "Entry_Time")
    private String entryTime;

    @Column(name = "Station_Id")
    private String stationId;
    @Column(name = "image")
    private String imageUrl;
    @Column(name = "Current_Id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private String currentId;
    @Column(name = "Formated_Weather_Time")
    private String formattedWeatherTime;
    @Column(name = "Max_Temp")
    private String maxTemperature;
    @Column(name = "Relative_Humidity")
    private String relativeHumidity;
    @Column(name = "Sun_Rise")
    private String sunRise;
    @Column(name = "Sun_Set")
    private String sunSet;
    @Column(name = "Sun_Rise_Hour")
    private String sunRiseHour;
    @Column(name = "Sun_Rise_Min")
    private String sunRiseMin;
    @Column(name = "Sun_Set_Hour")
    private String sunSetHour;
    @Column(name = "Sun_Set_Min")
    private String sunSetMin;
    @Column(name = "Formated_Weather_Date")
    private String formattedWeatherDate;
    @Column(name = "Precipitation_Noon")
    private String precipitationNoon;
    @Column(name = "Precipitation_Evening")
    private String precipitationEvening;
    @Column(name = "Precipitation_Tonight")
    private String precipitationTonight;
    @Column(name = "Wind_Speed")
    private String windSpeed;
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
    @Column(name = "Weather_Time")
    private String weatherTime;
    @Column(name = "Pressure_Mean_Sea_Level")
    private String pressureMeanSeaLevel;
    @Column(name = "Wind_Direction")
    private String windDirection;
    @Column(name = "Cloud")
    private String cloud;
    @Column(name = "Grid_Location")
    private String gridLocationId;
    @Column(name = "Pollen_Station")
    private String pollenStation;
    @Column(name = "Pollen_Total_Count")
    private String pollenTotalCount;
    @Column(name = "Pollen_Total_Percentage")
    private String pollenTotalPercentage;
    @Column(name = "Pollen_Total_Status")
    private String pollenTotalStatus;
    @Column(name = "Dew_Point")
    private String dewPoint;
    @Column(name = "Visibility")
    private String visibility;
    private List<PollenModel> pollenList;

    public CurrentModel() {
        super();
        this.minTemperature = "--";
        this.precipitation = "--";
        this.temperature = "--";
        this.entryTime = "--";
        this.stationId = "--";
        this.imageUrl = "";
        this.currentId = "--";
        this.formattedWeatherTime = "--";
        this.maxTemperature = "--";
        this.relativeHumidity = "--";
        this.sunRise = "--";
        this.sunSet = "--";
        this.setSunRiseHour("00");
        this.setSunRiseMin("00");
        this.setSunSetHour("00");
        this.setSunSetMin("00");
        this.formattedWeatherDate ="--";
        this.precipitationNoon = "--";
        this.precipitationEvening = "--";
        this.precipitationTonight = "--";
        this.windSpeed = "--";
        setFormattedHour("--");
        setFormattedMin("--");
        setFormattedDay("--");
        setFormattedMon("--");
        setFormattedYear("--");
        this.weatherTime = "--";
        this.pressureMeanSeaLevel = "--";
        this.windDirection = "--";
        this.cloud = "--";
        this.gridLocationId = "--";
        this.pollenStation = "--";
        this.pollenTotalCount = "--";
        this.pollenTotalPercentage = "--";
        this.pollenTotalStatus = "--";
        this.dewPoint = "--";
        this.visibility = "--";
        this.pollenList = getPollenList();
    }

    public String getDewPoint() {
        return dewPoint;
    }

    public void setDewPoint(String dewPoint) {
        this.dewPoint = dewPoint;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getPollenStation() {
        return pollenStation;
    }

    public void setPollenStation(String pollenStation) {
        this.pollenStation = pollenStation;
    }

    public String getPollenTotalCount() {
        return pollenTotalCount;
    }

    public void setPollenTotalCount(String pollenTotalCount) {
        this.pollenTotalCount = pollenTotalCount;
    }

    public String getPollenTotalStatus() {
        return pollenTotalStatus;
    }

    public void setPollenTotalStatus(String pollenTotalStatus) {
        this.pollenTotalStatus = pollenTotalStatus;
    }

    public List<PollenModel> getPollenList() {
        if (pollenList == null || pollenList.size() <= 0 && currentId!="--") {
            setPollenList(DbUtils.getPollenData(String.valueOf(currentId)));
        }
        return pollenList;
    }

    public void setPollenList(List<PollenModel> pollenList) {
        this.pollenList = pollenList;
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

    public String getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(String minTemperature) {
        this.minTemperature = minTemperature;
    }

    public String getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(String precipitation) {
        this.precipitation = precipitation;
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

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCurrentId() {
        return currentId;
    }

    public void setCurrentId(String currentId) {
        this.currentId = currentId;
    }

    public String getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(String maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public String getRelativeHumidity() {
        return relativeHumidity;
    }

    public void setRelativeHumidity(String relativeHumidity) {
        this.relativeHumidity = relativeHumidity;
    }

    public String getPrecipitationNoon() {
        return precipitationNoon;
    }

    public void setPrecipitationNoon(String precipitationNoon) {
        this.precipitationNoon = precipitationNoon;
    }

    public String getPrecipitationEvening() {
        return precipitationEvening;
    }

    public void setPrecipitationEvening(String precipitationEvening) {
        this.precipitationEvening = precipitationEvening;
    }

    public String getPrecipitationTonight() {
        return precipitationTonight;
    }

    public void setPrecipitationTonight(String precipitationTonight) {
        this.precipitationTonight = precipitationTonight;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getPressureMeanSeaLevel() {
        return pressureMeanSeaLevel;
    }

    public void setPressureMeanSeaLevel(String pressureMeanSeaLevel) {
        this.pressureMeanSeaLevel = pressureMeanSeaLevel;
    }

    public String getCloud() {
        return cloud;
    }

    public void setCloud(String cloud) {
        this.cloud = cloud;
    }

    public String getGridLocationId() {
        return gridLocationId;
    }

    public void setGridLocationId(String gridLocationId) {
        this.gridLocationId = gridLocationId;
    }

    public String getPollenTotalPercentage() {
        return pollenTotalPercentage;
    }

    public void setPollenTotalPercentage(String pollenTotalPercentage) {
        this.pollenTotalPercentage = pollenTotalPercentage;
    }

    public String getSunRise() {
        return sunRise;
    }

    public void setSunRise(String sunRise) {
        this.sunRise = sunRise;
    }

    public String getSunSet() {
        return sunSet;
    }

    public void setSunSet(String sunSet) {
        this.sunSet = sunSet;
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

    public String getWeatherTime() {
        return weatherTime;
    }

    public void setWeatherTime(String weatherTime) {
        this.weatherTime = weatherTime;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public void setDefault() {
        this.setSunRiseHour("--");
        this.setSunRiseMin("--");
        this.setSunSetHour("--");
        this.setSunSetMin("--");

    }
}
