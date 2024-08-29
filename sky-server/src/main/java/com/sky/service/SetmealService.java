package com.sky.service;

import com.sky.entity.Setmeal;
import com.sky.vo.DishItemVO;

import java.util.List;

/**
 * @author Marson
 * @date 2024/8/29
 */
public interface SetmealService {


    /**
     * 根据条件（分类id和状态）查询套餐
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);


    /**
     * 根据套餐id查询套餐中的菜品
     * @param id
     * @return
     */
    List<DishItemVO> getDishItemById(Long id);
}
