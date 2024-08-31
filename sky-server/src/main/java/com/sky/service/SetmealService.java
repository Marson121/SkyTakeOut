package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;

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


    /**
     * 新增套餐
     * @param setmealDTO
     */
    void saveWithDish(SetmealDTO setmealDTO);


    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);


    /**
     * 批量删除
     * @param ids
     */
    void deleteByIds(List<Long> ids);


    /**
     * 根据套餐id查询套餐及套餐菜品关系
     * @param id
     * @return
     */
    SetmealVO getByIdWithDish(Long id);


    /**
     * 修改套餐
     * @param setmealDTO
     */
    void update(SetmealDTO setmealDTO);


    /**
     * 修改套餐状态
     * @param status
     * @param setmealId
     */
    void startOrStop(Integer status, Long setmealId);
}
