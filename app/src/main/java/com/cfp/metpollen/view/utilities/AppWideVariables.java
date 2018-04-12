package com.cfp.metpollen.view.utilities;

import com.cfp.metpollen.data.db.model.StationModel;

import java.util.List;

/**
 * Created by AhmedAbbas on 11/16/2017.
 */

public class AppWideVariables {
    private static List<StationModel> stationList;

    public static List<StationModel> getStationList() {
        return stationList;
    }

    public static void setStationList(List<StationModel> stationList) {
        AppWideVariables.stationList = stationList;
    }
}
