package com.cfp.metpollen.data.db.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by AhmedAbbas on 12/7/2017.
 */

@Table(name = "Stations")
public class StationModel extends Model {
    @Column(name = "Station_Latitude")
    String stationLatitude;

    @Column(name = "Station_Longitude")
    String stationLongitude;

    @Column(name = "Station_Name")

    String dayImage;
    @Column(name = "Day_Image")

    String dayBlurImage;
    @Column(name = "Day_Blur_Image")


    String nightImage;
    @Column(name = "Night_Image")

    String nightBlurImage;
    @Column(name = "Night_Blur_Image")

    String stationName;
    @Column(name = "Station_Id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    int stationId;

    public StationModel() {
        super();
    }

    public StationModel(String stationLatitude, String stationLongitude, String stationName, int stationId) {
        super();
        this.stationLatitude = stationLatitude;
        this.stationLongitude = stationLongitude;
        this.stationName = stationName;
        this.stationId = stationId;
    }

    public String getDayImage() {
        return dayImage;
    }

    public void setDayImage(String dayImage) {
        this.dayImage = dayImage;
    }

    public String getDayBlurImage() {
        return dayBlurImage;
    }

    public void setDayBlurImage(String dayBlurImage) {
        this.dayBlurImage = dayBlurImage;
    }

    public String getNightImage() {
        return nightImage;
    }

    public void setNightImage(String nightImage) {
        this.nightImage = nightImage;
    }

    public String getNightBlurImage() {
        return nightBlurImage;
    }

    public void setNightBlurImage(String nightBlurImage) {
        this.nightBlurImage = nightBlurImage;
    }

    public String getStationLatitude() {
        return stationLatitude;
    }

    public void setStationLatitude(String stationLatitude) {
        this.stationLatitude = stationLatitude;
    }

    public String getStationLongitude() {
        return stationLongitude;
    }

    public void setStationLongitude(String stationLongitude) {
        this.stationLongitude = stationLongitude;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }
}
