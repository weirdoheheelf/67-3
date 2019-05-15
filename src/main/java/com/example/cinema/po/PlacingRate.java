package com.example.cinema.po;

public class PlacingRate {
    /**
     * 电影id
     */
    private Integer movieId;
    /**
     * 电影上座率
     */
    private double placingRate;
    /**
     * 电影名字
     */
    private String name;

    public Integer getId(){ return movieId;}

    public void setId(Integer movieId){ this.movieId=movieId;}

    public double getPlacingRate() {
        return placingRate;
    }

    public void setPlacingRate(double placingRate) {
        this.placingRate = placingRate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
