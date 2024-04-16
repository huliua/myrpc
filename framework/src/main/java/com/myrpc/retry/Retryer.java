package com.myrpc.retry;

import com.myrpc.domain.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 服务重试机制
 * @author huliua
 * @version 1.0
 * @date 2024-04-16 15:55
 */
@Slf4j
public class Retryer {

    /**
     * 最大重试次数
     */
    public static final int MAX_RETRY_TIMES = 3;
    /**
     * 重试间隔时间，单位：秒
     */
    public static final int RETRY_SLEEP_SECOND = 1;

    public static RpcResponse doRetry(Callable<?> callable) throws InterruptedException {
        RpcResponse res = new RpcResponse();
        int retryTimes = 0;
        while (retryTimes < MAX_RETRY_TIMES) {
            try {
                Object callResult = callable.call();
                res.setData(callResult);
                break;
            } catch (Exception e) {
                retryTimes++;
                log.info("retrying......retry times: {}", retryTimes);
                TimeUnit.SECONDS.sleep(RETRY_SLEEP_SECOND);
                res.setException(e);
            }
        }
        return res;
    }
}
