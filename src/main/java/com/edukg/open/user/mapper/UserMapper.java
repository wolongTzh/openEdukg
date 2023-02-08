package com.edukg.open.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.edukg.open.user.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author liufeifei
 * @since 2021-05-11
 */
@Mapper
public interface UserMapper extends BaseMapper<UserInfo> {

}
