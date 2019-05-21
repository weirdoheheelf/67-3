package com.example.cinema.blImpl.promotion;

import com.example.cinema.bl.promotion.VIPStrategyService;
import com.example.cinema.data.promotion.VIPStrategyMapper;
import com.example.cinema.po.VIPStrategy;
import com.example.cinema.vo.ResponseVO;
import com.example.cinema.vo.VIPStrategyForm;
import com.example.cinema.vo.VIPStrategyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VIPStrategyServiceImpl implements VIPStrategyService {
    @Autowired
    VIPStrategyMapper vipStrategyMapper;

    @Override
    public ResponseVO publishVIPStrategy(VIPStrategyForm vipStrategyForm){
        try {
            VIPStrategy vipStrategy=new VIPStrategy();
            vipStrategy.setName(vipStrategyForm.getName());
            vipStrategy.setGiftMoney(vipStrategyForm.getGiftMoney());
            vipStrategy.setPrice(vipStrategyForm.getPrice());
            vipStrategy.setTargetMoney(vipStrategyForm.getTargetMoney());
            vipStrategyMapper.insertOneVIPStrategy(vipStrategy);
            return ResponseVO.buildSuccess(vipStrategyMapper.selectVIPStrategyById(vipStrategy.getId()));
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO deleteVIPStrategy(int id){
        try {
            VIPStrategy vipStrategy=vipStrategyMapper.selectVIPStrategyById(id);
            if (vipStrategy!=null){
                vipStrategyMapper.deleteVIPStrategyById(id);
            }
            return ResponseVO.buildSuccess(vipStrategyMapper.selectVIPStrategies());
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO updateVIPStrategy(VIPStrategyForm vipStrategyForm){
        try {
            vipStrategyMapper.updateVIPStrategy(vipStrategyForm);
            return ResponseVO.buildSuccess();
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO getVIPStrategy(){
        try {
            List<VIPStrategyVO> vipStrategyVOList=new ArrayList<>();
            List<VIPStrategy> vipStrategyList=vipStrategyMapper.selectVIPStrategies();
            for (VIPStrategy vipStrategy:vipStrategyList){
                VIPStrategyVO vipStrategyVO=new VIPStrategyVO();
                vipStrategyVO.setId(vipStrategy.getId());
                vipStrategyVO.setName(vipStrategy.getName());
                vipStrategyVO.setGiftMoney(vipStrategy.getGiftMoney());
                vipStrategyVO.setTargetMoney(vipStrategy.getTargetMoney());
                vipStrategyVO.setPrice(vipStrategy.getPrice());
                vipStrategyVOList.add(vipStrategyVO);
            }
            return ResponseVO.buildSuccess(vipStrategyVOList);
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }
}
