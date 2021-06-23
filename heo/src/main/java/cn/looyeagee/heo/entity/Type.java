package cn.looyeagee.heo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * (Type)实体类
 *
 * @author makejava
 * @since 2020-06-01 23:18:53
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Type implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    
    private String typeName;




}