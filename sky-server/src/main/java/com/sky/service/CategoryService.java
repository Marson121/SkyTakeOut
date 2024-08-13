package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

/**
 * @author Marson
 * @date 2024/8/8
 */
public interface CategoryService {
    /**
     * 新增分类
     *
     * @param categoryDTO
     */
    void save(CategoryDTO categoryDTO);


    /**
     * 分类分页查询
     *
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);


    /**
     * 修改分类状态为启用或禁用
     *
     * @param status
     * @param id
     */
    void changeStatus(Integer status, Long id);


    /**
     * 根据类型查询
     *
     * @param type
     * @return
     */
    List<Category> getByType(Integer type);


    /**
     * 修改分类信息
     *
     * @param categoryDTO
     */
    void update(CategoryDTO categoryDTO);


    /**
     * 根据id删除
     *
     * @param id
     */
    void deleteById(Long id);
}
