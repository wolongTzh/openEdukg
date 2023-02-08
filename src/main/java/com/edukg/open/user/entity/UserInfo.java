package com.edukg.open.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author liufeifei
 * @since 2021-05-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("open_user_info")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @JsonIgnore
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String email;
    private String phone;
    @JsonIgnore
    private String passwordMd5;

    @TableField(value = "user_name")
    private String userName;

    @TableField(value = "user_type")
    private String userType;

    @JsonIgnore
    @TableField(value = "user_status")
    private Integer userStatus;


}
