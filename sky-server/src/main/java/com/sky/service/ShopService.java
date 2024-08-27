package com.sky.service;

/**
 * @author Marson
 * @date 2024/8/27
 */
public interface ShopService {

    /**
     * 修改店铺状态
     * @param status
     */
    void setShopStatus(Integer status);


    /**
     * 查看店铺状态
     * @return
     */
    Integer getShopService();
}
