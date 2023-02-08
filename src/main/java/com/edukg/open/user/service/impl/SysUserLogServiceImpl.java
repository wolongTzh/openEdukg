package com.edukg.open.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.edukg.open.user.entity.SysUserLog;
import com.edukg.open.user.mapper.SysUserLogMapper;
import com.edukg.open.user.service.ISysUserLogService;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liufeifei
 * @since 2021-05-11
 */
@Service
public class SysUserLogServiceImpl extends ServiceImpl<SysUserLogMapper, SysUserLog> implements ISysUserLogService {
    @Override
    public SysUserLog getbyUserId(Integer userId, String api){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = df.format(new Date());
        QueryWrapper<SysUserLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("api", api);
        queryWrapper.eq("created", currentTime);
        return getOne(queryWrapper);
    }
}
