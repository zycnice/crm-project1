package com.zyc.crm.commons.utils;

import java.util.UUID;

/**
 * @author zyc
 * @version 1.0
 */
public class UUIDUtils {

    /**
     * uuid随机生成32位字符
     * @return
     */
    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }
}
