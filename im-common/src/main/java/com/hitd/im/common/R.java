package com.hitd.im.common;

import com.hitd.im.common.exception.ApplicationExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ZhangWeinan
 * @date 2023-03-10 09:55
 * @description 公共响应类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class R<T> {

    private int code;

    private String msg;

    private T data;

    public static<T> R<T> successResponse(T data) {
        return new R<>(200, "success", data);
    }

    public static<T> R<T> successResponse() {
        return new R<>(200, "success");
    }

    public static<T> R<T> errorResponse() {
        return new R<>(500, "系统内部异常");
    }

    public static<T> R<T> errorResponse(int code, String msg) {
        return new R<>(code, msg);
    }

    public static<T> R<T> errorResponse(ApplicationExceptionEnum enums) {
        return new R<>(enums.getCode(), enums.getError());
    }

    public boolean isOk(){
        return this.code == 200;
    }


    public R(int code, String msg) {
        this.code = code;
        this.msg = msg;
//		this.data = null;
    }

    public R<T> success(){
        this.code = 200;
        this.msg = "success";
        return this;
    }

    public R<T> success(T data){
        this.code = 200;
        this.msg = "success";
        this.data = data;
        return this;
    }
    
}
