package com.edukg.open.user.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.edukg.open.user.entity.SysUserLog;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author liufeifei
 * @since 2021-02-02
 */
public interface ISysUserLogService extends IService<SysUserLog> {
    SysUserLog getbyUserId(Integer userId, String api);
}
