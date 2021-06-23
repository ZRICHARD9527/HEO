package cn.looyeagee.heo.mapper;

import cn.looyeagee.heo.entity.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author ：Z.Richard
 * @date ：Created in 2020/10/8 14:20
 * @description：
 * @modified By：
 * @version: 1.0
 */
public interface QuestionMapper extends BaseMapper<Question> {
    //获取标签下的问题
    @Select("SELECT q1.id,q1.tag_id ,q1.user_id, q1.content, q1.time, t1.img , t1.user_name FROM `question` q1 LEFT JOIN `user` t1 ON q1.user_id = t1.id WHERE is_show = 1 AND tag_id =#{tagId} limit #{begin},#{size}")
    List<Map<String,Object>> selectListByTagid(Integer tagId,Integer begin,Integer size);

    //获取相关的问题
    @Select("SELECT q1.id,q1.tag_id ,q1.user_id, q1.content, q1.time, t1.img , t1.user_name FROM `question` q1 LEFT JOIN `user` t1 ON q1.user_id = t1.id WHERE is_show = 1 AND q1.content RLIKE #{content} limit #{begin} ,#{size}")
    List<Map<String ,Object>> getListLike(String content,Integer begin,Integer size);

    //获取所有记录数
    @Select("SELECT q1.id,q1.tag_id ,q1.user_id, q1.content, q1.time, t1.img , t1.user_name FROM `question` q1 LEFT JOIN `user` t1 ON q1.user_id = t1.id limit #{begin},#{size}")
    List<Map<String,Object>> getList(Integer begin,Integer size);

    //获取搜索相关的总记录数
    @Select("SELECT count(*) from `question` where is_show=1 and content RLIKE #{content}")
    Integer countLike(String content);

    //获取标签下的总数
    @Select("SELECT count(*) from `question` where is_show=1 and tag_id = #{tagId}")
    Integer countTag(Integer tagId);


}
