package cn.looyeagee.heo.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Z.Richard
 * @CreateTime: 2020/10/14 22:26
 * @Description:
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Relation {
    @TableId(type = IdType.AUTO)
    Integer relId;

    Integer userId;

    Integer tagId;

    Integer clickNum;

    public Relation(Integer user_id, Integer tagId, int i) {
        this.userId = user_id;
        this.tagId = tagId;
        this.clickNum = i;
    }
}
