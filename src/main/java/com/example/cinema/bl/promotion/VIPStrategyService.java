package com.example.cinema.bl.promotion;

import com.example.cinema.vo.ResponseVO;
import com.example.cinema.vo.VIPStrategyForm;

public interface VIPStrategyService {
    /**
     * 发布新的VIP策略
     * @return
     */
    ResponseVO publishVIPStrategy(VIPStrategyForm vipStrategyForm);
    /**
     * 删除VIP策略
     * @return
     */
    ResponseVO deleteVIPStrategy(int id);
    /**
     * 更新VIP策略
     * @return
     */
    ResponseVO updateVIPStrategy(VIPStrategyForm vipStrategyForm);
    /**
     * 获取所有的VIP策略
     * @return
     */
    ResponseVO getVIPStrategy();
}
