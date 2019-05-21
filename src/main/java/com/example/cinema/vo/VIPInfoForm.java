package com.example.cinema.vo;

public class VIPInfoForm {
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
}
