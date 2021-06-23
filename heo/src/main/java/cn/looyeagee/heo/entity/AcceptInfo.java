package cn.looyeagee.heo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * (AcceptInfo)实体类
 *
 * @author makejava
 * @since 2020-06-01 22:49:06
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AcceptInfo implements Serializable {

    /**
    * 订单id
    */
    private Integer orderId;
    /**
     * 接收者id
     */

    private Integer userAccept;
    /**
    * 接收时间
    */
    private Date acceptDate;

    /**
    * 接收者完成时间
    */
    private Date accepterFinishDate;

    /**
    * 发布者确认时间
    */
    private Date publisherConfirmDate;

    /**
     * 截止提交时间
     */
    private Date limitDate;
}