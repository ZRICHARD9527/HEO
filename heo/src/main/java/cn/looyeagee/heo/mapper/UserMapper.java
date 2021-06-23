package cn.looyeagee.heo.mapper;

import cn.looyeagee.heo.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

public interface UserMapper extends BaseMapper<User> {
    @Update("UPDATE `user` SET `money` = #{money},`version`=#{newVersion} WHERE `id` = #{id} and `version` = #{oldVersion}")
    int updateMoneyByIdAndVersion(Integer id, BigDecimal money, Integer oldVersion,Integer newVersion);


}