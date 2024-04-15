package com.myrpc.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author huliua
 * @version 1.0
 * @date 2024-04-14 16:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResponseResult<T> implements Serializable {

    private String code;

    private String msg;

    private T data;

    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<>("200", "success", data);
    }

    public static <T> ResponseResult<T> fail(String code, String msg) {
        return new ResponseResult<>(code, msg, null);
    }
}
