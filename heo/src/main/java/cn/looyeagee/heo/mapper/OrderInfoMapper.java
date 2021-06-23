package cn.looyeagee.heo.mapper;


import cn.looyeagee.heo.entity.OrderInfo;
import cn.looyeagee.heo.entity.SelectInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface OrderInfoMapper extends BaseMapper<OrderInfo> {
    @Select("SELECT order_info.secret FROM order_info,accept_info WHERE order_info.id = accept_info.order_id and order_info.id = #{orderId} and accept_info.user_accept = #{userAccept};")
    Map<String,Object> selectSecretByOrderIdAndAcceptUserId(Integer orderId, Integer userAccept);


    @Select("SELECT `user`.user_name AS publish_user_name,`user`.`img`, type.type_name, order_info.*, accept_info.* FROM order_info LEFT JOIN accept_info ON order_info.id = accept_info.order_id LEFT JOIN type ON order_info.type_id = type.id LEFT JOIN `user` ON user_publish = `user`.id  WHERE user_publish = #{userId} AND user_accept IS NULL")
    List<Map<String,Object>> publishSelectNotAccept(Integer userId);
    @Select("SELECT `user`.user_name AS accept_user_name, `user`.`img`,type.type_name, order_info.*, accept_info.* FROM order_info LEFT JOIN accept_info ON order_info.id = accept_info.order_id LEFT JOIN type ON order_info.type_id = type.id LEFT JOIN `user` ON user_accept = `user`.id  WHERE user_publish = #{userId} AND user_accept IS NOT NULL AND accepter_finish_date IS NULL")
    List<Map<String,Object>>  publishSelectAccept(Integer userId);
    @Select("SELECT `user`.user_name AS accept_user_name, `user`.`img`,type.type_name, order_info.*, accept_info.* FROM order_info LEFT JOIN accept_info ON order_info.id = accept_info.order_id LEFT JOIN type ON order_info.type_id = type.id LEFT JOIN `user` ON user_accept = `user`.id  WHERE user_publish = #{userId} AND accepter_finish_date IS NOT NULL")
    List<Map<String,Object>>  publishSelectFinish(Integer userId);
    @Select("SELECT `user`.user_name AS publish_user_name, `user`.`img`,type.type_name, order_info.*, accept_info.* FROM order_info LEFT JOIN accept_info ON order_info.id = accept_info.order_id LEFT JOIN type ON order_info.type_id = type.id LEFT JOIN `user` ON user_publish = `user`.id  WHERE user_accept= #{userId} AND accepter_finish_date IS NULL")
    List<Map<String,Object>>  acceptSelectAcceptUnFinish(Integer userId);
    @Select("SELECT `user`.user_name AS publish_user_name, `user`.`img`,type.type_name, order_info.*, accept_info.* FROM order_info LEFT JOIN accept_info ON order_info.id = accept_info.order_id LEFT JOIN type ON order_info.type_id = type.id LEFT JOIN `user` ON user_publish = `user`.id  WHERE user_accept= #{userId} AND accepter_finish_date IS NOT NULL")
    List<Map<String,Object>>  acceptSelectFinish(Integer userId);

    List<List<?>> findResultByInfo(SelectInfo selectInfo);

}