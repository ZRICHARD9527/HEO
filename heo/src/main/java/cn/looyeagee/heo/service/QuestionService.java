package cn.looyeagee.heo.service;

import cn.looyeagee.heo.entity.Question;
import cn.looyeagee.heo.entity.Relation;
import cn.looyeagee.heo.entity.Tag;
import cn.looyeagee.heo.mapper.QuestionMapper;
import cn.looyeagee.heo.mapper.RelationMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;


/**
 * @author ：MXL
 * @author ：ZRichard
 * @date ：Created in 2020/10/8 14:16
 * @description：
 * @modified By：
 * @version: 1.0
 */
@Service
public class QuestionService {
    @Resource
    QuestionMapper questionMapper;
    @Resource
    RelationMapper relationMapper;

    public boolean insert(Integer userId, String content, Integer tagId) {
        Question question = new Question();
        question.setUserId(userId);
        question.setContent(content);
        question.setTagId(tagId);
        question.setTime(new Date());
        /**
         * ZRichard
         * 2020.10.13 添加is_show的值
         */
        question.setIsShow(1);
        try {
            return questionMapper.insert(question) > 0;
        } catch (Exception e) {
            return false;//外键错误
        }
    }

    /**
     * 更换更新方法
     *
     * @param questionId
     * @param user_id
     * @return
     */
    public boolean del(Integer questionId, Integer user_id) {
        Question question = questionMapper.selectById(questionId);
        question.setIsShow(0);
        if (question.getUserId() == user_id) {
            return questionMapper.updateById(question) > 0;
        } else {
            //说明用户id不正确
            return false;
        }
//        return questionMapper.update(question, new LambdaQueryWrapper<Question>().eq(Question::getUserId, user_id).eq(Question::getId, questionId)) > 0;
    }

    /**
     * 获取标签下的问题
     * 更换调用方法{因为需要更多信息+分页}
     *
     * @param tag_id
     * @return
     */
    public Map<String, Object> selectListByTagid(Integer tag_id, Integer page, Integer size) {
        Map<String, Object> map = new HashMap<>();
        map.put("question", questionMapper.selectListByTagid(tag_id, page * size, size));
        return map;
        //return questionMapper.selectList(new LambdaQueryWrapper<Question>().eq(Question::getTagId, tag_id));
    }

    /**
     * 获取所有问题
     * ZRichard
     * 2020.10.13
     *
     * @return
     */
    public Map<String, Object> getList(Integer page, Integer size) {
        Map<String, Object> map = new HashMap<>();
        map.put("question", questionMapper.getList(page * size, size));
        return map;

        //return questionMapper.selectList(new LambdaQueryWrapper<Question>().like(Question::getContent, content));
    }

    /**
     * 获取热门问题
     * MXL
     * 2020.10.13
     *
     * @return
     */
    public Map<String, Object> getList(Integer userId) {
        Map<String, Object> questions = null;
        //获取用户最喜欢的分类
        Integer tag_id = relationMapper.getHotTag(userId);
//        Relation relation = relationMapper.selectOne(new LambdaQueryWrapper<Relation>()
//                .orderByDesc(Relation::getClickNum)
//                .eq(Relation::getUserId, userId)
//                .or().eq(Relation::getUserId, null));

        //通过分类获取所有问题
        questions = selectListByTagid(tag_id, 0, 10);
        return questions;
        //return questionMapper.selectList(new LambdaQueryWrapper<Question>().like(Question::getContent, content));
    }

    /**
     * ZRichard
     * 获取未删除的总数
     * 2020.10.13
     *
     * @return
     */
    public Map<String, Object> count() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.gt("is_show", 0);
        Map<String, Object> map = new HashMap<>();
        map.put("total", questionMapper.selectCount(queryWrapper));
        return map;
    }

    /**
     * 获取相关的总数
     *
     * @return
     */
    public Map<String, Object> countLike(String content) {
        Map<String, Object> map = new HashMap<>();
        map.put("total", questionMapper.countLike(content));
        return map;
    }


    /**
     * 获取标签下的总数
     *
     * @return
     */
    public Map<String, Object> countTag(Integer tag_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("total", questionMapper.countTag(tag_id));
        return map;
    }

    /**
     * 获取相似问题的详情
     * ZRichard
     * 2020.10.13
     *
     * @return
     */
    public Map<String, Object> getListLike(String content, Integer page, Integer size) {
        Map<String, Object> map = new HashMap<>();
        map.put("question", questionMapper.getListLike(content, page * size, size));
        return map;
    }


    /**
     * 通过id获取问题详情
     * zrichard
     *
     * @return
     */
    public Question getQuestionById(Integer user_id, Integer id) {
        Question question = questionMapper.selectById(id);

        Relation relation = relationMapper.selectRel(user_id, question.getTagId());

        if (relation != null) {
            relation.setClickNum(relation.getClickNum() + 1);
            relationMapper.updateById(relation);
        } else {
            relationMapper.insert(new Relation(user_id, question.getTagId(), 1));
        }

        return questionMapper.selectById(id);
    }
}
