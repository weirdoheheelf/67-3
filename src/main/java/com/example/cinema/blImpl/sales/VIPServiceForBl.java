package com.example.cinema.blImpl.sales;

import com.example.cinema.po.VIPCard;

public interface VIPServiceForBl {
    /**
     * 根据UserId查找VIP卡
     * @param userId
     * @return
     */
    VIPCard getVIPCardByUserId(int userId);
}