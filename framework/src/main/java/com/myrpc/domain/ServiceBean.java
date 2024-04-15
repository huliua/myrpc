package com.myrpc.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author huliua
 * @version 1.0
 * @date 2024-04-14 17:54
 */
@Data
public class ServiceBean implements Serializable {

    private String beanName;

    private Class<?> beanClass;
}
