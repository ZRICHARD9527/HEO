package cn.looyeagee.heo.controller;


import cn.looyeagee.heo.result.ResultModel;
import cn.looyeagee.heo.result.ResultSuccess;
import cn.looyeagee.heo.service.TypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * (Type)表控制层
 *
 * @author makejava
 * @since 2020-06-01 22:49:10
 */
@RestController
@Api(tags = "互帮种类")
@RequestMapping("/type")
public class TypeController {
    /**
     * 服务对象
     */
    @Resource
   private TypeService typeService;
    @GetMapping("/all")
    public ResultModel getAllType(){
        return new ResultSuccess("ok",typeService.getAllType());
    }
}