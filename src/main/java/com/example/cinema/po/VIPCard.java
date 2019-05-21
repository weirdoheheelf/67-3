package com.example.cinema.po;


import com.example.cinema.vo.VIPCardVO;

import java.sql.Timestamp;

/**
 * Created by liying on 2019/4/14.
 */

public class VIPCard {

    /**
     * 目标金额
     */
    private double targetMoney;
    /**
     * 赠送金额
     */
    private double giftMoney;
    /**
     * 购买该vip卡所需金额
     */
    private double price;

    /**
     * 用户id
     */
    private int userId;

    /**
     * 会员卡id
     */
    private int id;

    /**
     * 会员卡余额
     */
    private double balance;

    /**
     * 办卡日期
     */
    private Timestamp joinDate;


    public VIPCard() {

    }

    public VIPCardVO toVO(){
        VIPCardVO vipCardVO=new VIPCardVO();
        vipCardVO.setGiftMoney(this.giftMoney);
        vipCardVO.setTargetMoney(this.targetMoney);
        vipCardVO.setPrice(this.price);
        vipCardVO.setBalance(this.balance);
        vipCardVO.setId(this.id);
        vipCardVO.setJoinDate(this.joinDate);
        vipCardVO.setUserId(this.userId);
        return vipCardVO;

    }

    public double getTargetMoney() {
        return targetMoney;
    }

    public void setTargetMoney(double targetMoney) {
        this.targetMoney = targetMoney;
    }

    public double getGiftMoney() {
        return giftMoney;
    }

    public void setGiftMoney(double giftMoney) {
        this.giftMoney = giftMoney;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Timestamp getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Timestamp joinDate) {
        this.joinDate = joinDate;
    }

    public double calculate(double amount) {
        return (int)(amount/200)*30+amount;

    }
}
