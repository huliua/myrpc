package com.myrpc.domain;

import cn.hutool.core.util.RandomUtil;
import lombok.Data;

import java.io.Serializable;

/**
 * 服务的元数据
 * @author huliua
 * @version 1.0
 * @date 2024-04-15 21:20
 */
@Data
public class ServiceMetaInfo implements Serializable {

    private String key;

    private String serviceName;

    private String host;

    private Integer port;

    private String version;
}
