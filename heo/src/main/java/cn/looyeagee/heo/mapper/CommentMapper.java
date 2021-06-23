package cn.looyeagee.heo.mapper;

import cn.looyeagee.heo.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface CommentMapper extends BaseMapper<Comment> {
    @Select("SELECT comment.id AS commentId, user_id, content, add_date, t1.img , t1.user_name AS pub_user_name, t2.user_name AS reply_name FROM `comment` LEFT JOIN `user` t1 ON user_id = t1.id LEFT JOIN `user` t2 ON reply = t2.id WHERE is_show = 1 AND order_id = #{orderId}")
    List<Map<String,Object>> getByOrderId(Integer orderId);
}
