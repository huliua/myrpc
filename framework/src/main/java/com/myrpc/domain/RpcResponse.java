package com.myrpc.domain;

import lombok.Data;

/**
 * 统一相应
 * @author huliua
 * @version 1.0
 * @date 2024-04-16 16:17
 */
@Data
public class RpcResponse {

    private Object data;

    private String msg;

    private Exception exception;
}
