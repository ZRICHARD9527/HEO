package cn.looyeagee.heo.controller;

import cn.looyeagee.heo.entity.Question;
import cn.looyeagee.heo.result.ResultFailure;
import cn.looyeagee.heo.result.ResultModel;
import cn.looyeagee.heo.result.ResultSuccess;
import cn.looyeagee.heo.service.AnswerService;
import cn.looyeagee.heo.service.QuestionService;
import cn.looyeagee.heo.service.TagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: Z.Richard
 * @CreateTime: 2020/10/8 10:49
 * @Description:
 **/
@Validated
@RestController
@RequestMapping(value = "/study")
@Api(tags = "问答社区")
@Slf4j
public class StudyController {


    @Autowired
    HttpServletRequest req;
    @Autowired
    AnswerService answerService;
    @Autowired
    QuestionService questionService;
    @Autowired
    TagService tagService;


    @ApiOperation(value = "发提问")
    @PostMapping("/question/{tag_id}")
    public ResultModel question(@ApiParam("标签id") @NotNull @PathVariable Integer tag_id
            , @ApiParam("提问内容") @RequestParam String content) {

        Integer userId = (Integer) req.getAttribute("userId");
        if (questionService.insert(userId, content, tag_id)) {
            return new ResultSuccess("提问成功");
        } else {
            return new ResultFailure("提问失败,请检查user_id和content是否正确");
        }
    }


    @ApiOperation(value = "删提问")
    @DeleteMapping("/question/{question_id}")
    public ResultModel delQuestion(@NotNull @PathVariable Integer question_id) {
        Integer userId = (Integer) req.getAttribute("userId");
        System.out.println("##################\nQ:" + question_id + "##################\nU:" + userId);
        if (questionService.del(question_id, userId)) {
            return new ResultSuccess("删除成功");
        } else {
            return new ResultFailure("删除失败,问题不存在或者您不是发布者(别捣乱)");
        }
    }

    @ApiOperation(value = "获取所有标签")
    @GetMapping("/tag/all")
    public ResultModel getAllTag() {
        return new ResultSuccess("获取成功", tagService.getAllTag());
    }


    @ApiOperation(value = "获取该标签下所有问题")
    @GetMapping("/tag/all/{tag_id}")
    public ResultModel question(@NotNull @PathVariable Integer tag_id,
                                @RequestParam(value = "currentPage") Integer currentPage,
                                @RequestParam(value = "size") Integer size) {
        Map<String, Object> data = questionService.selectListByTagid(tag_id, currentPage - 1, size);
        data.putAll(questionService.countTag(tag_id));
//        System.out.println(data.toString());
//        System.out.println("tagid:" + tag_id);
//        System.out.println("page:" + currentPage);
//        System.out.println("size:" + size);
        return new ResultSuccess("获取成功", data);
    }

    @ApiOperation(value = "搜索问题")
    @GetMapping("/all/like")
    public ResultModel question(@RequestParam(required = false) String content,
                                @RequestParam(value = "currentPage") Integer currentPage,
                                @RequestParam(value = "size") Integer size) {

        //存返回数据
        Map<String, Object> data = null;

        //没有内容则获取所有问题
        if (content == null) {
            data = questionService.getList(currentPage - 1, size);
            data.putAll(questionService.count());

        } else {
            data = questionService.getListLike(content.trim(), currentPage - 1, size);
            data.putAll(questionService.countLike(content));
        }

        System.out.println("**********************************请求问题***********************************************\n content : " + content);
        System.out.println(data.toString());
        return new ResultSuccess("获取成功", data);
    }


    @ApiOperation(value = "热门问题")
    @GetMapping("/all/fond")
    public ResultModel question() {
        Integer userId = (Integer) req.getAttribute("userId");
        //存返回数据
        Map<String, Object> questions = questionService.getList(userId);
        return new ResultSuccess("获取成功", questions);
    }


    //***********************************


    @ApiOperation(value = "获取问题详情")
    @GetMapping("/question/{question_id}")
    public ResultModel getQuestionById(@PathVariable("question_id") Integer questionId) {
        Integer userId = (Integer) req.getAttribute("userId");
        Object data = questionService.getQuestionById(userId, questionId);
        return new ResultSuccess("", data);
    }

    @ApiOperation(value = "获取回答")
    @GetMapping("/answer/{question_id}")
    public ResultModel getByQuestionId(@PathVariable("question_id") Integer question_id,
                                       @RequestParam(value = "currentPage") Integer currentPage,
                                       @RequestParam(value = "size") Integer size) {
        Integer userId = (Integer) req.getAttribute("userId");
        Map<String, Object> map = answerService.getAnswers(question_id, currentPage - 1, size,userId);
        System.out.println(map);
        if (map.get("answer") == null) {
            return new ResultFailure("暂无回答");
        } else {
            return new ResultSuccess("", map);
        }
    }

    @ApiOperation(value = "回答问题")
    @PostMapping("/answer/{question_id}")
    public ResultModel addAnswer(@PathVariable("question_id") Integer question_id,
                                 @RequestParam(value = "reply", required = false) Integer reply,
                                 @RequestParam String content) {

        Integer userId = (Integer) req.getAttribute("userId");

        if (answerService.addAnswer(userId, question_id, reply, content)) {
            return new ResultSuccess("发布成功");
        } else {
            return new ResultFailure("发布失败");
        }
    }

    @ApiOperation(value = "删除回答")
    @DeleteMapping("/answer/{answer_id}")
    public ResultModel delComment(@PathVariable("answer_id") Integer answer_id) {
        Integer userId = (Integer) req.getAttribute("userId");
        if (answerService.delAnswer(answer_id, userId)) {
            return new ResultSuccess("删除成功");
        } else {
            return new ResultFailure("删除失败,评论不存在或者您不是发布者(别捣乱)");
        }
    }


}
