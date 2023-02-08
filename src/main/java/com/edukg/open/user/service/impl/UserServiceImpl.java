package com.edukg.open.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.edukg.open.base.BusinessException;
import com.edukg.open.user.entity.UserInfo;
import com.edukg.open.user.entity.UserVerify;
import com.edukg.open.user.mapper.UserMapper;
import com.edukg.open.user.mapper.UserVerifyMapper;
import com.edukg.open.user.service.IUserService;
import com.edukg.open.util.Md5Util;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author liufeifei
 * @since 2021-05-11
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserInfo> implements IUserService {

    @Resource
    UserVerifyMapper userVerifyMapper;

    @Override
    public UserInfo findByEmail(String email) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        return getOne(queryWrapper);
    }

    @Override
    public UserInfo findByPhone(String phone) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        return getOne(queryWrapper);
    }


    @Override
    public Boolean checkEmail(String email) {
        if (email.contains("@") && findByEmail(email) == null) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean checkPhone(String value) {
        if (value.length() == 11 && findByPhone(value) == null) {
            return true;
        }
        return false;
    }

    @Override
    public UserInfo register(String phone, String email, String password, String userType, String userName, String validateCode) {
        Boolean validate = checkValidateCode(phone, validateCode);
        if (validate) {
            UserInfo user = new UserInfo();
            user.setPhone(phone);
            user.setEmail(email);
            user.setPasswordMd5(Md5Util.getMD5String(phone + password));
            user.setUserType(userType);
            user.setUserName(userName);
            if (save(user)) {
                return user;
            } else {
                throw new BusinessException(10001, "注册失败");
            }
        }
        return null;
    }

    @Override
    public Boolean editpw(String phone, String newpw, String validateCode) {
        Boolean validate = checkValidateCode(phone, validateCode);
        if (validate) {
            UserInfo one = findByPhone(phone);
            if (one != null) {
                one.setPasswordMd5(Md5Util.getMD5String(phone + newpw));
                updateById(one);
                return true;
            }
            return false;
        }
        return false;

    }

    private Boolean checkValidateCode(String phone, String validateCode) {
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //查询库数据
        QueryWrapper<UserVerify> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        queryWrapper.eq("code", validateCode);
        queryWrapper.eq("status", "0");
        UserVerify userVerify = userVerifyMapper.selectOne(queryWrapper);
        if (userVerify == null) {
            return false;
        } else {
            //存在置为无效
            try {
                userVerify.setStatus("1");
                userVerify.setUpdateTime(df.format(new Date()));
                userVerifyMapper.updateById(userVerify);
                String startTime = userVerify.getCreatTime();
                long i = new Date().getTime() - df.parse(startTime).getTime();
                if (i / (60 * 1000) > 5) {
                    return false;
                } else {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
