package cn.looyeagee.heo.controller;

import cn.looyeagee.heo.entity.OrderInfo;
import cn.looyeagee.heo.result.ResultFailure;
import cn.looyeagee.heo.result.ResultModel;
import cn.looyeagee.heo.result.ResultSuccess;
import cn.looyeagee.heo.result.ServiceResult;
import cn.looyeagee.heo.service.CommentService;
import cn.looyeagee.heo.service.OrderInfoService;
import cn.looyeagee.heo.util.SimpleGetObjectSample;
import cn.looyeagee.heo.util.Tools;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Validated
@RestController
@RequestMapping(value = "/action")
@Api(tags = "用户动作")
@Slf4j
public class ActionController {
    @Autowired
    OrderInfoService orderInfoService;
    @Autowired
    HttpServletRequest req;

    @Autowired
    CommentService commentService;
    private static final String PIC_SUFFIX = "^[(jpg)|(png)|(bmp)]+$";



    @ApiOperation(value = "用户接单")
    @GetMapping(value = "/acceptOrder/{orderId}")
    public ResultModel acceptOrder(
            @ApiParam(name = "orderId", value = "订单id", required = true) @PathVariable Integer orderId) {
        Integer id = (Integer) req.getAttribute("userId");
        System.out.println(orderId);
        System.out.println(id);
        ServiceResult<String> r = orderInfoService.acceptOrder(id, orderId);
        return new ResultModel(!r.isSuccess(), r.getMsg());

    }

    @ApiOperation(value = "用户完成订单")
    @GetMapping(value = "/finishOrder/{orderId}")
    public ResultModel finishOrder(
            @ApiParam(name = "orderId", value = "订单id", required = true) @PathVariable Integer orderId) {
        Integer id = (Integer) req.getAttribute("userId");
        ServiceResult<String> r = orderInfoService.finishOrder(id, orderId);
        return new ResultModel(!r.isSuccess(), r.getMsg());

    }

    @ApiOperation(value = "发布者确认完成订单")
    @GetMapping(value = "/confirmOrder/{orderId}")
    public ResultModel finalFinishOrder(
            @ApiParam(name = "orderId", value = "订单id", required = true) @PathVariable Integer orderId) {
        Integer id = (Integer) req.getAttribute("userId");
        ServiceResult<String> r = orderInfoService.confirmOrder(id, orderId);
        return new ResultModel(!r.isSuccess(), r.getMsg());


    }

    @ApiOperation(value = "获取隐私信息")
    @GetMapping(value = "/getSecret/{orderId}")
    public ResultModel getSecret(@ApiParam(name = "orderId", value = "订单id", required = true) @PathVariable Integer orderId) {
        Integer id = (Integer) req.getAttribute("userId");
        ServiceResult<String> r = orderInfoService.getSecret(id,orderId);
        return new ResultModel(!r.isSuccess(), r.getMsg());
    }

    @ApiOperation(value = "发布订单")
    @PostMapping(value = "/submitOrder")
    public ResultModel submitOrder(@Validated OrderInfo orderInfo) {
        Integer id = (Integer) req.getAttribute("userId");
        orderInfo.setUserPublish(id);//把当前登录的人设置为发布者
        ServiceResult<String> r = orderInfoService.submitOrder(orderInfo);
        return new ResultModel(!r.isSuccess(), r.getMsg());
    }

    @ApiOperation(value = "发布者取消订单")
    @GetMapping(value = "/cancelOrder/{orderId}")
    public ResultModel cancelOrder(
            @ApiParam(name = "orderId", value = "订单id", required = true) @PathVariable Integer orderId) {
        Integer id = (Integer) req.getAttribute("userId");
        ServiceResult<String> r = orderInfoService.cancelOrder(id, orderId);
        return new ResultModel(!r.isSuccess(), r.getMsg());
    

    }
    @ApiOperation(value = "上传图片到OSS")
    @PostMapping(value ="/uploadPic")
    public ResultModel upload(@ApiParam(name = "file", value = "图片", required = true)
                                           MultipartFile file) throws IOException {

        // 没得产品图直接返回错误
        if (file == null || file.isEmpty())
            return new ResultFailure("没选择图片");
        String suffix;
        if ((suffix = Tools.isSuffixMatch(file.getOriginalFilename(), PIC_SUFFIX)) == null) {
            return new ResultFailure("拓展名不正确");
        }

        File temp = File.createTempFile( "heo",  "heo");//创建临时文件
        file.transferTo(temp);
        String result_url = SimpleGetObjectSample.upload(temp, req.getAttribute("userId").toString(),"."+suffix);
        if(result_url==null){
            return new ResultFailure("上传失败");
        }else{
            return new ResultSuccess("上传成功",result_url);
        }
    }
    @ApiOperation(value = "找评论")
    @GetMapping("/comment/{order_id}")
    public ResultModel findComment(@NotNull @PathVariable Integer order_id){
        return new ResultSuccess("ok",commentService.selectByOrderId(order_id));
    }
    @ApiOperation(value = "发评论")
    @PostMapping("/comment/{order_id}")
    public ResultModel comment(@NotNull @PathVariable Integer order_id, @ApiParam("回复谁") @RequestParam(required = false) Integer reply, @RequestParam String content){
        Integer userId = (Integer) req.getAttribute("userId");
        if(commentService.insert(userId,order_id,content,reply)){
            return new ResultSuccess("发布成功");
        }else{
            return new ResultFailure("发布失败,请检查order_id和reply是否正确");
        }
    }
    @ApiOperation(value = "删评论")
    @DeleteMapping("/comment/{commentId}")
    public ResultModel delComment(@NotNull @PathVariable Integer commentId){
        Integer userId = (Integer) req.getAttribute("userId");
        if(commentService.del(commentId,userId)){
            return new ResultSuccess("删除成功");
        }else{
            return new ResultFailure("删除失败,评论不存在或者您不是发布者(别捣乱)");
        }
    }
}
