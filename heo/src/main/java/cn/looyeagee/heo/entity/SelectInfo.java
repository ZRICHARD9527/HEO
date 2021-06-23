package cn.looyeagee.heo.entity;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;

@Data
public class SelectInfo implements Serializable {
    @ApiParam(name = "pageNumber", value = "第几页", example = "1")
    @NotNull
    @Min(value = 1, message = "页码必须大于0")
    Integer pageNumber;

    @ApiParam(name = "pageSize", value = "一页几个数据", example = "20")
    @NotNull
    @Min(value = 1, message = "数据大小必须大于0")
    @Max(value = 20, message = "数据大小必须小于等于20")
    Integer pageSize;

    @ApiParam(name = "title", value = "模糊标题")
    String title;

    @DecimalMin(value = "0.001", message = "最小积分不能低于0")
    @ApiParam(name = "minMoney", value = "最小积分")
    Double minMoney;

    @DecimalMin(value = "0.001", message = "最大积分不能低于0")
    @ApiParam(name = "maxMoney", value = "最大积分")
    Double maxMoney;

    @ApiParam(name = "type", value = "类别")
    Long type;

    @ApiParam(name = "userPublish", value = "谁发布的")
    Long userPublish;

    @ApiParam(name = "highToLow", value = "是否降序")
    Integer highToLow;

    @ApiParam(name = "orderBy", value = "通过什么字段排序")
    String orderBy;

}
