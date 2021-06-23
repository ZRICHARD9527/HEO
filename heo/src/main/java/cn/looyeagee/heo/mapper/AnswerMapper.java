package cn.looyeagee.heo.mapper;

import cn.looyeagee.heo.entity.Answer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface AnswerMapper extends BaseMapper<Answer> {
    //获取问题下的所有回答
@Select("SELECT" +
        " a1.id AS pub_id," +
        " a1.user_id AS pub_user_id, " +
        " a1.content AS pub_content, " +
        " a1.add_date AS pub_time, " +
        " a2.is_show AS rpl_is_show, " +
        " a1.reply AS rpl_id, " +
        " a2.user_id AS rpl_user_id, " +
        " a2.content AS rpl_content, " +
        " a2.add_date AS rpl_time, " +
        " t1.img AS pub_img, " +
        " t1.user_name AS pub_name, " +
        " t2.img AS rpl_img, " +
        " t2.user_name AS rpl_name " +
        " FROM " +
        " `answer` a1 " +
        " LEFT JOIN `answer` a2 ON a1.reply = a2.id " +
        " LEFT JOIN `user` t1 ON a1.user_id = t1.id " +
        " LEFT JOIN `user` t2 ON a2.user_id = t2.id " +
        " WHERE " +
        " a1.is_show = 1 " +
        " AND a1.question_id = #{questionId}"+
        " order by a1.add_date DESC limit #{begin} , #{size}")
    List<Map<String,Object>> getByQuestionId(Integer questionId,Integer begin,Integer size);
    //获取问题下回答总数
    @Select("SELECT count(*) from `answer` where question_id=#{questionId} and is_show=1")
    Integer count(Integer questionId);


}
