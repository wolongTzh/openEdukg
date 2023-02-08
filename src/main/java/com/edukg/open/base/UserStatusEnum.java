package com.edukg.open.base;

public enum UserStatusEnum {


    ADMIN(9, "管理员"),
    SCOMMON(2, "审核过的普通用户"),
    COMMON(1, "普通用户"),
    LOCKED(0, "被锁");
    public Integer code;

    public String message;

    UserStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }


}
