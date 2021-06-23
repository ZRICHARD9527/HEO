package cn.looyeagee.heo.service;

import cn.looyeagee.heo.entity.*;
import cn.looyeagee.heo.mapper.OrderInfoMapper;
import cn.looyeagee.heo.result.ServiceResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OrderInfoService {
    @Resource
    UserService userService;
    @Resource
    TypeService typeService;
    @Resource
    OrderInfoMapper orderInfoMapper;

    @Resource
    AcceptInfoService acceptInfoService;

    public ServiceResult<String> submitOrder(OrderInfo o) {
        String msg;
        boolean flag = false;
        Date now = new Date();
        User user = userService.findUserById(o.getUserPublish());
        if (typeService.findTypeById(o.getTypeId()) == null) {
            msg = "类型不存在";
        } else if (user.getMoney().compareTo(o.getOrderMoney()) < 0) {
            msg = "可用余额不足";
        } else {
            //设置开始时间
            o.setBeginDate(now);
            //设置积分
            BigDecimal subtract = user.getMoney().subtract(o.getOrderMoney());// CAS
            userService.updateUser(user);
            if (userService.updateMoneyByIdAndVersion(o.getUserPublish(), subtract, user.getVersion(), user.getVersion() + 1)) {
                o.setId(null);
                orderInfoMapper.insert(o);
                flag = true;
                msg = "发布成功！当前余额为" + subtract;
            } else {
                msg = "服务器繁忙，请稍后再试！";
            }

        }
        return new ServiceResult<>(flag, msg);
    }

    public OrderInfo findOrderById(Integer id) {
        return orderInfoMapper.selectById(id);
    }

    public ServiceResult<String> acceptOrder(Integer userId, Integer orderId) {
        //User user = userService.findUserById(userId);
        OrderInfo order = findOrderById(orderId);
        String msg;
        boolean flag = false;
        Date now = new Date();
        //double freezeMoney;
        if (order == null) {
            msg = "获取订单失败";
        } else if (order.getUserPublish().equals(userId)) {
            msg = "自己接自己的单干啥...";
        } else if (order.getEndDate().compareTo(now) < 0) {
            msg = "该单已过期！";
        } else if (acceptInfoService.isAccept(orderId)) {
            msg = "已被别人接单！";
        } else {
            //已经设置order_id 索引是 UNIQUE 不必担心插入多条重复接单
            AcceptInfo acceptInfo = new AcceptInfo();
            acceptInfo.setOrderId(orderId);
            acceptInfo.setAcceptDate(now);
            acceptInfo.setUserAccept(userId);
            Date limitDate = (Date) now.clone();
            limitDate.setTime(limitDate.getTime() + (order.getLimitHour() * 60 * 60 * 1000));
            acceptInfo.setLimitDate(limitDate);
            if (acceptInfoService.insert(acceptInfo)) {
                flag = true;
                msg = "接单成功";
            } else {
                msg = "慢了一步,已被别人接单！";
            }
        }
        return new ServiceResult<>(flag, msg);
    }

    public ServiceResult<String> finishOrder(Integer userId, Integer orderId) {
        AcceptInfo accept = acceptInfoService.findAcceptInfoByOrderId(orderId);
        String msg;
        boolean flag = false;
        Date now = new Date();
        if (accept == null) {
            msg = "获取信息失败";
        } else if (!userId.equals(accept.getUserAccept())) {
            msg = "不是自己的单，不能完成哦！";
        } else if (accept.getAccepterFinishDate() != null) {
            msg = "已经完成过了！";
        } else {
            String resultMsg;
            if (accept.getLimitDate().compareTo(now) < 0) {
                resultMsg = "超时完成,请尽可能在规定时间内完成!超时次数+1,达到一定次数将限制接单";
            } else {
                resultMsg = "完成成功，请等待发布者确认";
            }
            accept.setAccepterFinishDate(now);
            if (acceptInfoService.update(accept)) {
                msg = resultMsg;
                flag = true;
            } else {
                msg = "操作失败!";
            }

        }
        return new ServiceResult<>(flag, msg);

    }

    public ServiceResult<String> confirmOrder(Integer userId, Integer orderId) {
        AcceptInfo accept = acceptInfoService.findAcceptInfoByOrderId(orderId);
        OrderInfo order = findOrderById(orderId);
        Integer userAcceptId;
        User userAccept;
        String msg;
        boolean flag = false;
        Date now = new Date();
        if (accept == null) {
            msg = "订单不存在！";
        } else if (!order.getUserPublish().equals(userId)) {
            msg = "该单发布者不是自己！";
        } else if ((userAcceptId = accept.getUserAccept()) == null) {
            msg = "该单还没人接，无法确认完成";
        } else if ((userAccept = userService.findUserById(userAcceptId)) == null) {
            msg = "找不着接单的用户信息了";
        } else if (accept.getAccepterFinishDate() == null) {
            msg = "接单者还没提交完成申请，无法确认完成";
        } else {

            BigDecimal orderMoney = order.getOrderMoney();

            //给用户加积分
            Integer oldVersion = userAccept.getVersion();
            if (userService.updateMoneyByIdAndVersion(userAcceptId, userAccept.getMoney().add(orderMoney), oldVersion, oldVersion + 1)) {
                flag = true;
                msg = "完成成功";
                accept.setPublisherConfirmDate(now);
                acceptInfoService.update(accept);
            } else {
                msg = "服务器繁忙,请稍后再试!";
            }
        }
        return new ServiceResult<>(flag, msg);
    }

    public String selectSecretByOrderIdAndAcceptUserId(Integer orderId, Integer userAccept) {
        Map<String, Object> m = orderInfoMapper.selectSecretByOrderIdAndAcceptUserId(orderId, userAccept);
        if (m != null && m.containsKey("secret")) {
            return m.get("secret") == null ? "暂无或获取失败" : m.get("secret").toString();
        }
        return "暂无或获取失败";
    }

    public ServiceResult<String> getSecret(Integer userId, Integer orderId) {
        return new ServiceResult<>(true, selectSecretByOrderIdAndAcceptUserId(orderId, userId));
    }

    public ServiceResult<String> cancelOrder(Integer userId, Integer orderId) {
        OrderInfo order = findOrderById(orderId);
        User userPublish = userService.findUserById(userId);
        String msg;
        boolean flag = false;
        if (order == null) {
            msg = "订单不存在！";
        } else if (!order.getUserPublish().equals(userId)) {
            msg = "该单发布者不是自己！";
        } else if (acceptInfoService.isAccept(orderId)) {
            msg = "该单已被接，无法取消";
        } else {
            BigDecimal orderMoney = order.getOrderMoney();
            BigDecimal money = userPublish.getMoney().add(orderMoney);
            if (userService.updateMoneyByIdAndVersion(userId, money, userPublish.getVersion(), userPublish.getVersion() + 1)) {
                orderInfoMapper.deleteById(orderId);
                flag = true;
                msg = "取消成功," + orderMoney + "积分已经返回您的余额。";
            } else {
                msg = "服务器繁忙,请稍后再试!";
            }
        }
        return new ServiceResult<>(flag, msg);

    }

    public List<Map<String, Object>> getOrderStatus(Integer userId, Integer type) {
        switch (type) {
            case 1:
                return orderInfoMapper.publishSelectNotAccept(userId);
            case 2:
                return orderInfoMapper.publishSelectAccept(userId);
            case 3:
                return orderInfoMapper.publishSelectFinish(userId);
            case 4:
                return orderInfoMapper.acceptSelectAcceptUnFinish(userId);
            case 5:
                return orderInfoMapper.acceptSelectFinish(userId);
        }
        return null;
    }

    public List<Map<String, Object>> query(

    ) {
        return null;
    }

    public MyPage<Map<String, Object>> getPageNew(SelectInfo selectInfo) {
        selectInfo.setPageNumber((selectInfo.getPageNumber() - 1) * selectInfo.getPageSize());//mysql 从第0页开始
        System.out.println(selectInfo);
        List<List<?>> findtest = orderInfoMapper.findResultByInfo(selectInfo);
        List<Map<String, Object>> orderinfos = (List<Map<String, Object>>) findtest.get(0);
        int totalCount = (Integer) findtest.get(1).get(0);
        log.debug("当前页面记录数：" + orderinfos.size());
        orderinfos.forEach(x -> x.remove("secret"));
        log.debug("符合条件记录数：" + totalCount);
        int totalPagesCount = (totalCount + selectInfo.getPageSize() - 1) / selectInfo.getPageSize();
        log.debug("总页数：" + totalPagesCount);
        return new MyPage<>(totalPagesCount, orderinfos.size(), totalCount, orderinfos);
    }
}
