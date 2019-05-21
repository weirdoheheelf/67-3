package com.example.cinema.data.promotion;

import com.example.cinema.po.VIPStrategy;
import com.example.cinema.vo.VIPStrategyForm;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface VIPStrategyMapper {

    int insertOneVIPStrategy(VIPStrategy vipStrategy);

    int deleteVIPStrategyById(int id);

    int updateVIPStrategy(VIPStrategyForm vipStrategyForm);

    List<VIPStrategy> selectVIPStrategies();

    VIPStrategy selectVIPStrategyById(int id);
}
