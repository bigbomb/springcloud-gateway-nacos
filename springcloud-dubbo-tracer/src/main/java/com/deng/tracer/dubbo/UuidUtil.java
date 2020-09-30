package com.deng.tracer.dubbo;

import java.util.UUID;

/**
 * @author bigbomb
 * @version 1.0
 * @date 2020/2/20 14:06
 * @className UuidUtil
 * @desc UUID工具类
 */
class UuidUtil {

    public static String getUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
