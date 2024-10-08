package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Marson
 * @date 2024/8/8
 */
@Mapper
public interface CategoryMapper {

    /**
     * 插入数据
     *
     * @param category
     */
    @Insert("insert into category(id, type, name, sort, status, create_time, update_time, create_user, update_user)" +
            "values " +
            "(null, #{type}, #{name}, #{sort}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    @AutoFill(value = OperationType.INSERT)
    void insert(Category category);


    /**
     * 分类分页查询
     *
     * @param categoryPageQueryDTO
     * @return
     */
    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);


    /**
     * 修改
     * @param category
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Category category);


    /**
     * 根据类型查询
     * @param type
     * @return
     */
    List<Category> selectByType(Integer type);


    /**
     * 根据id删除
     * @param id
     */
    @Delete("delete from  category where id = #{id}")
    void delete(Long id);
}
