package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author Marson
 * @date 2024/8/8
 */
@Mapper
public interface DishMapper {

    /**
     * 统计当前分类下的菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 插入数据
     * @param dish
     */
    @AutoFill(OperationType.INSERT)
    void insert(Dish dish);
}
