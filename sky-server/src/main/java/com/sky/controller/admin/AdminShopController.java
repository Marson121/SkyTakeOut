package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ShopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Marson
 * @date 2024/8/27
 */
@RestController
@RequestMapping("/admin/shop")
@Api(tags = "店铺相关方法")
@Slf4j
public class AdminShopController {

    @Autowired
    private ShopService shopService;

    /**
     * 修改店铺状态
     *
     * @param status
     * @return
     */
    @PutMapping("/{status}")
    @ApiOperation("修改店铺状态")
    public Result setStatus(@PathVariable Integer status) {
        log.info("设置店铺的营业状态为：{}", status == 1 ? "营业中" : "打烊中");
        shopService.setShopStatus(status);
        return Result.success();
    }


    /**
     * 查看店铺状态
     *
     * @return
     */
    @GetMapping("/status")
    @ApiOperation("查看店铺状态")
    public Result<Integer> getStatus() {
        Integer status = shopService.getShopService();
        if (status != null) {
            log.info("获取到店铺的营业状态为：{}", status == 1 ? "营业中" : "打烊中");
        } else {
            log.info("未获取到店铺的营业状态");
        }
        return Result.success(status);
    }
}
