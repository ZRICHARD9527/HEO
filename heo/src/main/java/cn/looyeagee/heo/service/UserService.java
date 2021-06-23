package cn.looyeagee.heo.service;

import cn.looyeagee.heo.entity.User;
import cn.looyeagee.heo.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Service
@Slf4j
public class UserService {
    @Resource
    UserMapper userMapper;

    public User findUserByOpenid(String openid) {
        return userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getOpenid, openid));
    }

    public boolean insertUser(User user, String openid, String userName, String img, Integer sex) {
        user.setOpenid(openid);
        user.setUserName(userName);
        user.setImg(img);
        user.setSex(sex);
        user.setMoney(new BigDecimal("100")); // fixme:正式上线为0元
        user.setVersion(0);
        user.setRegDate(new Date());
        return userMapper.insert(user) > 0;
    }

    public User findUserById(Serializable id) {
        return userMapper.selectById(id);
    }

    public void updateUser(User user) {
        userMapper.updateById(user);
    }

    public int addMoney(Integer userId, Integer m) {
        User user = userMapper.selectById(userId);
        BigDecimal decimal = new BigDecimal(m.toString());

        BigDecimal subtract = user.getMoney().add(decimal);
        user.setMoney(subtract);

        return userMapper.updateMoneyByIdAndVersion(userId, subtract, user.getVersion(), user.getVersion() + 1);
    }

    public boolean updateMoneyByIdAndVersion(Integer id, BigDecimal money, Integer oldVersion, Integer newVersion) {
        return userMapper.updateMoneyByIdAndVersion(id, money, oldVersion, newVersion) > 0;
    }


}
