package cn.looyeagee.heo.controller;

import cn.looyeagee.heo.entity.MyPage;
import cn.looyeagee.heo.entity.SelectInfo;
import cn.looyeagee.heo.result.ResultFailure;
import cn.looyeagee.heo.result.ResultModel;
import cn.looyeagee.heo.result.ResultSuccess;
import cn.looyeagee.heo.service.OrderInfoService;
import cn.looyeagee.heo.util.Tools;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@Validated
@RestController
@RequestMapping(value = "/order")
@Api(tags = "订单查询")
@Slf4j
public class OrderController {
    @Resource
    OrderInfoService orderInfoService;
    @ApiOperation(value = "查询所有订单", notes = "")
    @PostMapping(value = "/query")
    @SuppressWarnings("all")
    public ResultModel newinfo(@Validated SelectInfo selectInfo  ) {
        MyPage<Map<String,Object>> page = orderInfoService.getPageNew(selectInfo);
        if (page.getCurrentRecordsCount() != 0) {
            return new ResultSuccess("获取成功", page);
        } else {
            return new ResultFailure("获取失败");
        }

    }
}
