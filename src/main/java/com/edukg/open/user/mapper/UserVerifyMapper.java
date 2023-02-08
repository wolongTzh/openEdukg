package com.edukg.open.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.edukg.open.user.entity.UserVerify;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户验证码表 Mapper 接口
 * </p>
 *
 * @author wangjh
 * @since 2021-05-12
 */
@Mapper
public interface UserVerifyMapper extends BaseMapper<UserVerify> {

}
