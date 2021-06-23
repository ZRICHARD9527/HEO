package cn.looyeagee.heo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * (Comment)实体类
 *
 * @author makejava
 * @since 2020-06-07 11:07:11
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Comment implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
    * 订单id
    */
    private Integer orderId;
    /**
    * 用户id
    */
    private Integer userId;
    /**
    * 内容
    */
    private String content;
    /**
    * 添加日期
    */
    private Date addDate;
    /**
    * 是否是回复谁
    */
    private Integer reply;
    /**
    * 是否展示
    */
    @JsonIgnore
    private Integer isShow;



}