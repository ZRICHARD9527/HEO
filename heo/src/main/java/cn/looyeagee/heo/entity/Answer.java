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
 * @CreateTime: 2020/10/8 11:46
 * @Description:
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)//用来去除数据中的空值

public class Answer {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer questionId;
    private Integer userId;
    private String content;
    private Date addDate;
    private Integer reply;
    @JsonIgnore
    private Integer isShow = 1;

    public Answer(Integer userId, Integer questionId, String content) {
        this.userId = userId;
        this.questionId = questionId;
        this.content = content;
        this.addDate = new Date();//当前时间
        this.reply = 0;//表示为回答问题
    }

    public Answer(Integer userId, Integer questionId, String content, Integer reply) {
        this.userId = userId;
        this.questionId = questionId;
        this.content = content;
        this.addDate = new Date();//当前时间
        this.reply = reply;//表示为讨论
    }

}
