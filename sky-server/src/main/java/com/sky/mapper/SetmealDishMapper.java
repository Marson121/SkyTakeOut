package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Marson
 * @date 2024/8/18
 */
@Mapper
public interface SetmealDishMapper {

    /**
     * 根据多个菜品id查询套餐
     * @param ids
     * @return
     */
    List<Long> getSetmealIdsByDishIds(List<Long> ids);
}
