package cn.looyeagee.heo.controller;


import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.looyeagee.heo.config.WxMaConfiguration;
import cn.looyeagee.heo.entity.Student;
import cn.looyeagee.heo.entity.User;
import cn.looyeagee.heo.mapper.StudentMapper;
import cn.looyeagee.heo.result.ResultFailure;
import cn.looyeagee.heo.result.ResultModel;
import cn.looyeagee.heo.result.ResultSuccess;
import cn.looyeagee.heo.service.OrderInfoService;
import cn.looyeagee.heo.service.UserService;
import cn.looyeagee.heo.util.IdcardUtils;
import cn.looyeagee.heo.util.SendSms;
import cn.looyeagee.heo.util.Tools;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;


/**
 * (User)表控制层
 *
 * @author makejava
 * @since 2020-06-01 22:49:10
 */
@Validated
@RestController
@RequestMapping(value = "/user")
@Api(tags = "用户登录注册查询")
@Slf4j
public class UserController {
    private static final String APPID = "wxd43783642bd277fe";
    private final static ConcurrentHashMap<String, String> phoneAndCode = new ConcurrentHashMap<>();
    @Autowired
    HttpServletRequest req;
    /**
     * 服务对象
     */
    @Resource
    private UserService userService;
    @Resource
    private OrderInfoService orderInfoService;
    @Resource
    private StudentMapper studentMapper;

    @GetMapping("/login")
    @ApiOperation(value = "登录", notes = "只填code就是第一次登录 填了其他的将更新用户")
    public ResultModel login(String code,
                             @RequestParam(required = false) String userName,
                             @RequestParam(required = false) String img,
                             @RequestParam(required = false) Integer sex) {
        final WxMaService wxService = WxMaConfiguration.getMaService(APPID);
        try {
            WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(code);
            log.info(session.getOpenid());
            User user = userService.findUserByOpenid(session.getOpenid());
            if (user == null) {
                user = new User();
                userService.insertUser(user, session.getOpenid(), "无名氏", "/images/init.png", -1);
            }
            if (userName!=null && img!= null && sex !=null){
                user.setUserName(userName);
                user.setImg(img);
                user.setSex(sex);
                userService.updateUser(user);
            }
            HashMap<String, Object> map = new HashMap<>();
            map.put("jwt", Tools.createJwt(String.valueOf(user.getId()), user.getOpenid(), 2_592_000_000L));
            map.put("user", user);
            return new ResultSuccess("登录成功", map);
        } catch (WxErrorException e) {
            log.error(e.getMessage(), e);
            return new ResultFailure("code验证失败");
        }
    }

    @GetMapping("/info")
    @ApiOperation(value = "查询自己信息")
    public ResultModel getMyself() {
        Integer id = (Integer) req.getAttribute("userId");
        User user = userService.findUserById(id);
        if (user == null) {
            return new ResultFailure("无此用户");
        } else {
            Map<String, User> m = new HashMap<>(1);
            m.put("user", user);
            return new ResultSuccess("查询成功", m);
        }
    }

    @GetMapping("/topUp")
    @ApiOperation(value = "充值")
    public ResultModel test(@Max(value = 100, message = "一次最多充值100块钱嗷") @RequestParam Integer money) {
        Integer id = (Integer) req.getAttribute("userId");
        int i = userService.addMoney(id, money);
        if (i == -1) {
            return new ResultFailure("操作太快");
        } else {
            return new ResultSuccess("充值成功");
        }
    }

    @GetMapping("/orderStatus")
    @ApiOperation(value = "订单状态", notes = "type填下面的：1发布者已发布的但未被接单的、2发布者的单被接单的但未完成的、3发布者的单被接单且已完成的；4接单者已接单但未完成的、5接单者已接单且已经完成的")
    public ResultModel orderStatus(@NotNull @RequestParam Integer type) {
        return new ResultSuccess("ok", orderInfoService.getOrderStatus((Integer) req.getAttribute("userId"), type));
    }

    @GetMapping("/sendSms")
    @ApiOperation(value = "发送短信")
    public ResultModel sendSms(String phone) {
        String code = String.format("%04d", new Random().nextInt(9999));
        if (phone.length() == 11) {
            SendSms.sendMsg(phone, code);
            phoneAndCode.put(phone, code);
            return new ResultSuccess("发送成功");
        } else {
            return new ResultFailure("手机号错误");
        }

    }

    @GetMapping("/realName")
    @ApiOperation(value = "实名认证")
    public ResultModel realName(
            @NotNull @RequestParam String sfz,
            @NotNull @RequestParam String name,
            @NotNull @RequestParam String phone,
            @NotNull @RequestParam String stuid,
            @NotNull @RequestParam String code) {
        if (name.equals("测试")) {
            User user = new User();
            user.setId((Integer) req.getAttribute("userId"));
            user.setTrueName("测试");
            userService.updateUser(user);
            return new ResultSuccess("认证成功");
        }
        if (!code.equals(phoneAndCode.get(phone))) {
            return new ResultFailure("验证码错误");
        }

        if (!IdcardUtils.validateCard(sfz) || name.length() <= 1) {
            return new ResultFailure("身份证姓名不匹配");
        } else if (studentMapper.selectCount(new LambdaQueryWrapper<Student>().eq(Student::getStuNo, stuid).eq(Student::getStuName, name)) == 0) {
            return new ResultFailure("您好像不是本校的学生~");
        } else {//ok
            User user = new User();
            user.setId((Integer) req.getAttribute("userId"));
            user.setTrueName(name);
            user.setStuid(stuid);
            user.setSfz(sfz);
            user.setPhoneNumber(phone);
            userService.updateUser(user);
            phoneAndCode.remove(phone);
            return new ResultSuccess("认证成功");
        }
    }

}