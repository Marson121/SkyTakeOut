package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Marson
 * @date 2024/8/18
 */
@Mapper
public interface SetmealDishMapper {

    /**
     * 根据多个菜品id查询对应的套餐
     * @param ids
     * @return
     */
    List<Long> getSetmealIdsByDishIds(List<Long> ids);


    /**
     * 批量插入数据
     * @param setmealDishes
     */
    void insertBatch(List<SetmealDish> setmealDishes);
}
