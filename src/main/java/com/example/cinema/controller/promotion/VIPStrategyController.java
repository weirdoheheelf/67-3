package com.example.cinema.controller.promotion;

import com.example.cinema.bl.promotion.VIPStrategyService;
import com.example.cinema.vo.ResponseVO;
import com.example.cinema.vo.VIPStrategyForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vipStrategy")
public class VIPStrategyController {
    @Autowired
    VIPStrategyService vipStrategyService;

    @PostMapping("/publish")
    public ResponseVO publishVIPStrategy(@RequestBody VIPStrategyForm vipStrategyForm){
        return vipStrategyService.publishVIPStrategy(vipStrategyForm);
    }
    @PostMapping("/delete")
    public ResponseVO deleteVIPStrategy(@RequestParam int id){
        return vipStrategyService.deleteVIPStrategy(id);
    }
    @PostMapping("update")
    public ResponseVO updateVIPStrategy(@RequestBody VIPStrategyForm vipStrategyForm){
        return vipStrategyService.updateVIPStrategy(vipStrategyForm);
    }
    @GetMapping("get")
    public ResponseVO getVIPStrategy(){
        return vipStrategyService.getVIPStrategy();
    }
}
