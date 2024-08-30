package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Marson
 * @date 2024/8/29
 */
@RestController
@RequestMapping("/user/dish")
@Api(tags = "用户端菜品浏览接口")
public class UserDishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<DishVO>> list(Long categoryId) {

        // 1.构造redis中的key
        String key = "dish_" + categoryId;

        // 2.查询redis中是否有缓存
        List<DishVO> list = (List<DishVO>) redisTemplate.opsForValue().get(key);
        if (list != null && !list.isEmpty()) {
            // 3.有缓存则直接返回，不查数据库
            return Result.success(list);
        }


        // 4.没有缓存则查询数据库
        Dish dish = new Dish();
        // 设置条件：分类id 状态
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);

        list = dishService.listWithFlavor(dish);

        // 5.将查询出的数据放入redis
        redisTemplate.opsForValue().set(key, list);

        return Result.success(list);
    }

}
