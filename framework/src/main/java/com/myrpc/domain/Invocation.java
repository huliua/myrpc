package com.myrpc.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author huliua
 * @version 1.0
 * @date 2024-04-14 17:38
 */
@Data
public class Invocation implements Serializable {

    private String serviceName;

    private String className;

    private String methodName;

    private Class[] paramTypes;

    private Object[] args;

    private Class returnType;
}
