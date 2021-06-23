package cn.looyeagee.heo.service;

import cn.looyeagee.heo.entity.Answer;
import cn.looyeagee.heo.entity.Question;
import cn.looyeagee.heo.entity.Relation;
import cn.looyeagee.heo.mapper.AnswerMapper;
import cn.looyeagee.heo.mapper.QuestionMapper;
import cn.looyeagee.heo.mapper.RelationMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Z.Richard
 * @CreateTime: 2020/10/8 14:17
 * @Description:
 **/

//问答服务
@Service
public class AnswerService {
    @Resource
    AnswerMapper answerMapper;
    @Resource
    RelationMapper relationMapper;
    @Resource
    QuestionMapper questionMapper;

    /**
     * 获取所有的回答
     *
     * @param question_id
     * @return
     */
    public Map<String, Object> getAnswers(Integer question_id, Integer page, Integer size,Integer user_id) {
        Map<String, Object> map = new HashMap<>();

        //获取所有回答
        List<Map<String, Object>> list = answerMapper.getByQuestionId(question_id, page * size, size);

        Question question = questionMapper.selectById(question_id);
        Relation relation = relationMapper.selectRel(user_id, question.getTagId());

        if (relation != null) {
            relation.setClickNum(relation.getClickNum() + 1);
            relationMapper.updateById(relation);
        } else {
            relationMapper.insert(new Relation(user_id, question.getTagId(), 1));
        }

        //遍历list,修改已删除回答的内容
        for (Map<String, Object> m : list
        ) {
            if (m.get("rpl_is_show") != null && Integer.parseInt(m.get("rpl_is_show").toString()) == 0) {
                m.put("rpl_content", "该回答已删除");
                m.remove("rpl_is_show");
            }
        }

        map.put("answer", list);
        map.put("total", answerMapper.count(question_id));

        return map;
    }


    /**
     * 回答问题
     *
     * @param userId     回答者
     * @param questionId 绑定问题id
     * @param content    回答内容
     * @return
     */
    public boolean addAnswer(Integer userId, Integer questionId, Integer reply, String content) {

        Answer answer = null;
        if (reply == null) {
            answer = new Answer(userId, questionId, content);
        } else {
            answer = new Answer(userId, questionId, content, reply);
        }
        try {
            System.out.println(answer.toString());
            return answerMapper.insert(answer) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("addAnswer-error");
            return false;
        }
    }

    /**
     * 删除回答
     *
     * @param id      回答Id
     * @param user_id 用户Id
     * @return
     */
    public boolean delAnswer(Integer id, Integer user_id) {

        Answer answer = answerMapper.selectById(id);

        answer.setIsShow(0);
        if (answer.getUserId() == user_id) {
            return answerMapper.updateById(answer) > 0;
        } else {
            return false;
        }
//        return answerMapper.update(answer, new LambdaQueryWrapper<Answer>().eq(Answer::getUserId, user_id).eq(Answer::getId, id)) > 0;

    }
}
