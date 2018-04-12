package com.cfp.metpollen.data.db.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by AhmedAbbas on 1/19/2018.
 */
@Table(name = "Pollen")
public class PollenModel extends Model{

    @Column(name = "Name")
    String name;

    @Column(name = "Status")
    String status;

    @Column(name = "Count")
    String count;

    @Column(name = "Current_Id")
    String currentId;

    public PollenModel(String name, String status, String count, String currentId) {
        this.name = name;
        this.status = status;
        this.count = count;
        this.currentId = currentId;
    }

    public PollenModel() {
        super();
        this.name = "--";
        this.status = "--";
        this.count = "--";
        this.currentId = "--";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getCurrentId() {
        return currentId;
    }

    public void setCurrentId(String currentId) {
        this.currentId = currentId;
    }
}
