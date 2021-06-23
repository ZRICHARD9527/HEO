package cn.looyeagee.heo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: Z.Richard
 * @CreateTime: 2020/10/8 10:52
 * @Description:
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)//用来去除数据中的空值
public class Question {

    /**
     * 提问id
     * 声明为id自增mp则不会自动分配id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 提问用户id
     */
    private Integer userId;
    /**
     * 提问内容
     */
    private String content;

    /**
     * 标签名
     */
    private Integer tagId;

    /**
     * 提问时间
     */
    private Date time;

    /**
     * 是否展示
     * 此注解在实体类向前台返回数据时用来忽略不想传递给前台的属性或接口
     */
    @JsonIgnore
    private Integer isShow;

}
