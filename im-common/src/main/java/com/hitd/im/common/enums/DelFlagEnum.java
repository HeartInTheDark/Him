package com.hitd.im.common.enums;

/**
 * @author ZhangWeinan
 * @date 2023-03-08 11:27
 * @description
 */
public enum DelFlagEnum {

    /**
     * 0 正常；1 删除。
     */
    NORMAL(0),

    DELETE(1),
    ;

    private int code;

    DelFlagEnum(int code){
        this.code=code;
    }

    public int getCode() {
        return code;
    }
}
