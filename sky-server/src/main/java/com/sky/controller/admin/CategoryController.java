package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Marson
 * @date 2024/8/8
 */
@RestController
@RequestMapping("/admin/category")
@Slf4j
@Api(tags = "分类相关方法")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     *
     * @param categoryDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增分类")
    public Result save(@RequestBody CategoryDTO categoryDTO) {
        categoryService.save(categoryDTO);

        return Result.success();
    }


    /**
     * 分类分页查询
     *
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("分类分页查询")
    public Result<PageResult> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageResult pageResult = categoryService.pageQuery(categoryPageQueryDTO);

        return Result.success(pageResult);
    }


    /**
     * 修改分类状态为启用或禁用
     *
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("修改分类状态为启用或禁用")
    public Result changeStatus(@PathVariable Integer status, Long id) {
        categoryService.changeStatus(status, id);

        return Result.success();
    }


    /**
     * 根据类型查询
     *
     * @param type
     * @return
     */
    // todo 不知道这个是干啥的
    @GetMapping("/list")
    @ApiOperation("根据类型查询")
    public Result<List<Category>> getByType(Integer type) {
        List<Category> list = categoryService.getByType(type);
        return Result.success(list);
    }


    // todo 不知道回显在哪做的

    /**
     * 修改分类信息
     *
     * @param categoryDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改分类信息")
    public Result update(@RequestBody CategoryDTO categoryDTO) {
        categoryService.update(categoryDTO);

        return Result.success();
    }

    /**
     * 删除分类
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation("删除分类")
    public Result deleteById(Long id) {
        categoryService.deleteById(id);
        return Result.success();
    }
}
