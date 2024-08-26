package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Marson
 * @date 2024/8/17
 */
@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 新增菜品和对应的口味
     * @param dishDTO
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {

        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        // 1.向菜品表插入一条数据
        dishMapper.insert(dish);

        Long id = dish.getId(); // 前端传过来的数据没有dish_id，通过mapper.xml设置插入数据后将id封装到对象，在此便可以获取id

        // 2.向菜品口味表插入多条数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(id);
            });
            dishFlavorMapper.insertBatch(flavors);
        }

    }


    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);

        long total = page.getTotal();
        List<DishVO> records = page.getResult();

        return new PageResult(total, records);
    }

    /**
     * 批量删除菜品
     * @param ids
     */
    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        // 1.当前菜品状态为启售不能删除
        for (Long id: ids) {
            Dish dish = dishMapper.getById(id);
            if (dish.getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        // 2.当前菜品关联了套餐不能删除
        List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
        if (setmealIds != null && !setmealIds.isEmpty()) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        // 3.删除菜品以及口味表中的数据
        // for (Long id: ids) {
        //     dishMapper.deleteById(id);
        //     dishFlavorMapper.deleteByDishId(id);
        // }


        // 优化：批量删除，上述代码会发送多条sql，可能引发性能问题
        // 批量删除菜品
        dishMapper.deleteByIds(ids);
        // 批量删除菜品口味
        dishFlavorMapper.deleteByDishIds(ids);
    }


    /**
     * 根据ids查询菜品及其口味
     * @param id
     * @return
     */
    @Override
    public DishVO getByIdWithFlavor(Long id) {

        // 1.根据菜品id查询dish
        Dish dish = dishMapper.getById(id);
        // 2.根据菜品id查询对应的口味
        List<DishFlavor> dishFlavors = dishFlavorMapper.getByDishId(id);
        // 3.拷贝到DishVO
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        // 4.设置flavor属性
        dishVO.setFlavors(dishFlavors);

        return dishVO;
    }


    /**
     * 修改菜品及其口味信息
     * @param dishDTO
     */
    @Override
    @Transactional
    public void updateWithFlavor(DishDTO dishDTO) {
        // 1.修改菜品表基本信息
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.update(dish);

        // 2.修改菜品口味信息
        // 2.1 删除原有的口味信息
        dishFlavorMapper.deleteByDishId(dishDTO.getId());

        // 2.2 重新插入口味信息
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishDTO.getId());
            });
            // 2.3 批量插入新的口味信息
            dishFlavorMapper.insertBatch(flavors);
        }
    }
}
