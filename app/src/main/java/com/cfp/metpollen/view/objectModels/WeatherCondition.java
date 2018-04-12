package com.cfp.metpollen.view.objectModels;

/**
 * Created by AhmedAbbas on 1/9/2018.
 */

public class WeatherCondition {
    private String condition;
    private int resourceId;

    public WeatherCondition(String condition, int resourceId) {
        this.condition = condition;
        this.resourceId = resourceId;
    }

    public WeatherCondition() {

    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }
}
