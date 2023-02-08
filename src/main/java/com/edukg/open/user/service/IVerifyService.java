package com.edukg.open.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.edukg.open.user.entity.UserVerify;

/**
 * <p>
 * 信息表 服务类
 * </p>
 *
 * @author wangjh
 * @since 2021-05-12
 */
public interface IVerifyService extends IService<UserVerify> {

    Boolean validate(String appId, String secretKey, String host,
                     String phone, String smsCode);

}
