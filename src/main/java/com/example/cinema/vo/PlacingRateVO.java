package com.example.cinema.vo;

import java.util.Date;
import java.util.List;

public class PlacingRateVO {
    /**
     * 电影名称
     */
    private String name;
    /**
     * 电影上座率
     */
    private String placingRateByDate;
    /**
     * 日期
     */
    private Date date;

    public PlacingRateVO(){
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlacingRateByDate() {
        return placingRateByDate;
    }

    public void setPlacingRateByDate(String placingRateByDate) {
        this.placingRateByDate = placingRateByDate;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
