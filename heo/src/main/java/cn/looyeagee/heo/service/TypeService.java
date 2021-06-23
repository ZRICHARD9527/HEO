package cn.looyeagee.heo.service;

import cn.looyeagee.heo.entity.Type;
import cn.looyeagee.heo.mapper.TypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeService {

    @Autowired
    TypeMapper typeMapper;

    public String findTypeById(Integer id) {
        Type type = typeMapper.selectById(id);
        if (type == null) {
            return null;
        } else {
            return type.getTypeName();
        }
    }

    public List<Type> getAllType() {
        return typeMapper.selectList(null);
    }
}