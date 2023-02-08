package com.edukg.open.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.edukg.open.user.entity.UserInfo;


/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author liufeifei
 * @since 2020-06-04
 */
public interface IUserService extends IService<UserInfo> {

    UserInfo findByEmail(String email);

    UserInfo findByPhone(String Phone);


    Boolean checkEmail(String email);


    Boolean checkPhone(String value);

    UserInfo register(String phone, String email, String password, String userType, String userName, String validateCode);

    Boolean editpw(String phone, String newpw, String validateCode);
}
