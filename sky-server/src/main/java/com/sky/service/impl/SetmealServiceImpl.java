package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Marson
 * @date 2024/8/29
 */
@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private DishMapper dishMapper;

    /**
     * 根据条件（分类id和状态）查询套餐
     * @param setmeal
     * @return
     */
    @Override
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    /**
     * 根据套餐id查询套餐中的菜品
     * @param id
     * @return
     */
    @Override
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }


    /**
     * 新增套餐ji
     * @param setmealDTO
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);

        // 1.向套餐表插入数据
        setmealMapper.insert(setmeal);

        // 插入数据库后获取套餐id
        Long id = setmeal.getId();

        // 2.向套餐菜品关系表插入数据
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> {
            // 给套餐菜品关系表中设置套餐id
            setmealDish.setSetmealId(id);
        });
        setmealDishMapper.insertBatch(setmealDishes);
    }

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());

        Page<SetmealVO> page =  setmealMapper.pageQuery(setmealPageQueryDTO);

        return new PageResult(page.getTotal(), page.getResult());
    }


    /**
     * 批量删除
     * @param ids
     */
    @Override
    public void deleteByIds(List<Long> ids) {
        ids.forEach(setmealId -> {
            // 判断当前套餐是否正在起售
            Setmeal setmeal = setmealMapper.getById(setmealId);
            if (setmeal.getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        });

        // 如果是未起售的套餐，则删除套餐及套餐菜品关系表中的数据
        ids.forEach(setmealId -> {
            setmealMapper.deleteById(setmealId);
            setmealDishMapper.deleteBySetmealId(setmealId);
        });

    }

    /**
     * 根据套餐id查询套餐及套餐菜品关系
     * @param id
     * @return
     */
    @Override
    public SetmealVO getByIdWithDish(Long id) {
        Setmeal setmeal = setmealMapper.getById(id);
        List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(id);

        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);

        return setmealVO;
    }


    /**
     * 修改套餐
     * @param setmealDTO
     */
    @Override
    @Transactional
    public void update(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);

        // 1.修改套餐表基础信息
        setmealMapper.update(setmeal);

        // 获取当前套餐id，用于删除旧的套餐菜品关系，给新插入的套餐菜品关系中的套餐id赋值
        Long setmealId = setmealDTO.getId();

        // 2.修改套餐菜品关系表，对于关系选择先删除后插入的方法
        // 2.1 删除旧的套餐菜品关系
        setmealDishMapper.deleteBySetmealId(setmealId);
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish-> {
            // 2.2 给新的套餐菜品关系赋套餐id
            setmealDish.setSetmealId(setmealId);
        });
        // 2.3 重新插入新的套餐菜品关系
        setmealDishMapper.insertBatch(setmealDishes);
    }


    /**
     * 修改套餐状态
     * @param status
     * @param setmealId
     */
    @Override
    public void startOrStop(Integer status, Long setmealId) {
        // 起售套餐时，如果套餐中包含有停售菜品则无法起售套餐
        if (status == StatusConstant.ENABLE) {
            List<Dish> dishList = dishMapper.getBySetmealId(setmealId);
            if (dishList != null && !dishList.isEmpty()) {
                dishList.forEach(dish -> {
                    if (StatusConstant.DISABLE == dish.getStatus()) {
                        throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                });
            }
        }

        // 停售直接修改，不需要上述判断
        Setmeal setmeal = Setmeal.builder()
                .id(setmealId)
                .status(status)
                .build();

        setmealMapper.update(setmeal);
    }
}
