package cn.looyeagee.heo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.io.Serializable;

/**
 * (User)实体类
 *
 * @author makejava
 * @since 2020-06-01 23:18:53
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
    * wechat openid
    */
    private String openid;
    /**
    * 同步wechat
    */
    private String userName;
    /**
    * 学校id
    */
    private Integer school;
    /**
    * 学号
    */
    private String stuid;
    /**
    * 真实姓名
    */
    private String trueName;
    /**
    * 手机号 
    */
    private String phoneNumber;
    /**
    * 现在金额
    */
    private BigDecimal money;

    /**
    * 1男 0女
    */
    private Integer sex;
    /**
    * 注册时间
    */
    private Date regDate;
    /**
    * 同步wechat
    */
    private String img;

    private String sfz;

    private Integer version;


}