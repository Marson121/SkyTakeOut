package com.sky.controller.user;

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
@RequestMapping("/user/shop")
@Api(tags = "店铺相关方法")
@Slf4j
public class UserShopController {

    @Autowired
    private ShopService shopService;

    /**
     * 查看店铺状态
     *
     * @return
     */
    @GetMapping("/status")
    @ApiOperation("查看店铺状态")
    public Result<Integer> getStatus() {
        Integer status = shopService.getShopService();
        log.info("获取到店铺的营业状态为：{}", status == 1 ? "营业中" : "打烊中");
        return Result.success(status);
    }
}
