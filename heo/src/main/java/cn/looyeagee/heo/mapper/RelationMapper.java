package cn.looyeagee.heo.mapper;

import cn.looyeagee.heo.entity.Relation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

public interface RelationMapper extends BaseMapper<Relation> {
    //通过用户以及tag id获取关系
    @Select("select * from `relation` where (user_id =#{userId} and tag_id=#{tagId}) limit 0,1")
    Relation selectRel(Integer userId, Integer tagId);

    //获取用户最喜欢的分类
    @Select("select tag_id from `relation` where user_id=#{userId}  ORDER BY click_num DESC limit 0,1")
    Integer getHotTag(Integer userId);
}
