package com.example.cinema.blImpl.sales;

import com.example.cinema.po.Activity;

import java.util.List;

public interface ActivityServiceForBl {
    /**
     * 根据电影id查找优惠劵
     * @param movieId
     * @return
     */
    List<Activity> getActivitiesByMovie(int movieId);
}
