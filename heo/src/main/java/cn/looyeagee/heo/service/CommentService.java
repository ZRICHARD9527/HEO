package cn.looyeagee.heo.service;

import cn.looyeagee.heo.entity.Comment;
import cn.looyeagee.heo.mapper.CommentMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CommentService {
    @Resource
    CommentMapper commentMapper;
    public List<Map<String, Object>> selectByOrderId(Integer orderId){
        return commentMapper.getByOrderId(orderId);
    }

    public boolean insert(Integer userId,Integer order_id, String content, Integer reply){
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setAddDate(new Date());
        comment.setContent(content);
        comment.setOrderId(order_id);
        comment.setReply(reply);
        try {
            return commentMapper.insert(comment) > 0 ;
        }catch (Exception e){
            return false;//外键错误
        }
    }
    public boolean del(Integer commentId,Integer user_id){
        Comment comment = new Comment();
        comment.setIsShow(0);
        return commentMapper.update(comment,new LambdaQueryWrapper<Comment>().eq(Comment::getUserId,user_id).eq(Comment::getId,commentId)) > 0;
    }
}
