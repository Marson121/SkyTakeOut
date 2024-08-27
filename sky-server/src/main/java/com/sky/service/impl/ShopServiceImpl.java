package com.sky.service.impl;

import com.sky.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Marson
 * @date 2024/8/27
 */
@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private RedisTemplate redisTemplate;

    public static final String KEY = "SHOP_STATUS";

    /**
     * 修改店铺状态
     *
     * @param status
     */
    @Override
    public void setShopStatus(Integer status) {
        redisTemplate.opsForValue().set(KEY, status);
    }


    /**
     * 查看店铺状态
     *
     * @return
     */
    @Override
    public Integer getShopService() {
        return (Integer) redisTemplate.opsForValue().get(KEY);
    }
}
