package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

/**
 * @author Marson
 * @date 2024/8/17
 */
public interface DishService {

    /**
     * 新增菜品和对应的口味
     * @param dishDTO
     */
    public void saveWithFlavor(DishDTO dishDTO);


    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);


    /**
     * 批量删除菜品
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据id查询菜品及其口味
     * @param id
     * @return
     */
    DishVO getByIdWithFlavor(Long id);


    /**
     * 修改菜品及其口味信息
     * @param dishDTO
     */
    void updateWithFlavor(DishDTO dishDTO);


    /**
     * 条件查询菜品及其口味
     * 条件：分类id 状态
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);


    /**
     * 修改菜品状态
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);


    /**
     * 根据分类id查询其包含的菜品
     * @param categoryId
     * @return
     */
    List<Dish> list(Long categoryId);
}
