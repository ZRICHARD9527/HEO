package cn.looyeagee.heo.service;

import cn.looyeagee.heo.entity.Tag;
import cn.looyeagee.heo.entity.Type;
import cn.looyeagee.heo.mapper.QuestionMapper;
import cn.looyeagee.heo.mapper.TagMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ：MXL
 * @date ：Created in 2020/10/8 15:45
 * @description：标签Service类
 * @modified By：
 * @version: 1.0
 */
@Service
public class TagService {
    @Resource
    TagMapper tagMapper;

    public List<Tag> getAllTag() {
        return tagMapper.selectList(null);
    }
}
