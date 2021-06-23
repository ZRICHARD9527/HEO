package cn.looyeagee.heo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;
import java.io.Serializable;

/**
 * (OrderInfo)实体类
 *
 * @author makejava
 * @since 2020-06-01 22:49:10
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderInfo implements Serializable {
    /**
    * 订单id
    */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "订单id", hidden = true)
    private Integer id;

    /**
    * 订单名称
    */
    @ApiModelProperty(value = "订单标题", required = true)
    @NotBlank(message = "订单标题不能为空")
    @Size(min = 3, max = 20, message = "订单标题必须为3-20个字符")
    private String title;

    /**
    * 订单类型
    */
    @ApiModelProperty(value = "订单类型", required = true)
    @NotNull(message = "订单类型不能为空")
    private Integer typeId;

    /**
    * 详细描述
    */
    @ApiModelProperty(value = "订单详情", required = true)
    @NotBlank(message = "订单详情不能为空")
    @Size(min = 1, max = 500, message = "订单详情必须为1-500个字符")
    private String detail;

    /**
    * 隐藏信息
    */
    @ApiModelProperty(value = "订单隐私信息")
    private String secret;

    /**
    * 积分
    */
    @ApiModelProperty(value = "积分设置", required = true)
    @NotNull(message = "积分设置不能为空")
    @DecimalMin(value = "0.001", message = "最小积分不能低于0")
    private BigDecimal orderMoney;

    /**
    * 开始时间
    */
    @ApiModelProperty(value = "开始时间", hidden = true)//开始时间系统生成
    private Date beginDate;

    /**
    * 截止接单时间
    */
    @ApiModelProperty(value = "停止接单时间", required = true)
    @NotNull(message = "停止接单时间不能为空")
    @Future(message = "停止接单时间要大于现在")
    private Date endDate;

    /**
    * 限时任务 接单后计时
    */
    @ApiModelProperty(value = "接单后限制时间", required = true)
    @NotNull(message = "接单后限制时间不能为空")
    @Min(value = 1,message = "限制时间至少设置为1小时")
    private Integer limitHour;

    /**
    * 谁发布的
    */
    @ApiModelProperty(value = "发布者", hidden = true)
    private Integer userPublish;

    //图片url
    @ApiModelProperty(value = "图片url")
    private String picUrl;




}