package cn.looyeagee.heo.service;

import cn.looyeagee.heo.entity.AcceptInfo;
import cn.looyeagee.heo.mapper.AcceptInfoMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 2020.10.08
 * ZRichard
 * 添加注释
 */
//接单服务
@Service
public class AcceptInfoService {
    @Resource
    AcceptInfoMapper acceptInfoMapper;

    public boolean isAccept(Integer orderId){
        return acceptInfoMapper.selectCount(new LambdaQueryWrapper<AcceptInfo>().eq(AcceptInfo::getOrderId,orderId)) > 0;
    }
    public boolean insert(AcceptInfo acceptInfo){
        try {
            return acceptInfoMapper.insert(acceptInfo) > 0;
        }catch (Exception e){//已经接单了
            e.printStackTrace();
            return false;
        }

    }
    public AcceptInfo findAcceptInfoByOrderId(Integer orderId){
        return acceptInfoMapper.selectOne(new LambdaQueryWrapper<AcceptInfo>().eq(AcceptInfo::getOrderId,orderId));
    }
    public boolean update(AcceptInfo acceptInfo){
        return  acceptInfoMapper.update(acceptInfo,new LambdaQueryWrapper<AcceptInfo>().eq(AcceptInfo::getOrderId,acceptInfo.getOrderId())) > 0;
        //return acceptInfoMapper.updateById(acceptInfo) > 0;
    }
}
