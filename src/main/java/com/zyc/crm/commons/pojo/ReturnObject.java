package com.zyc.crm.commons.pojo;

import lombok.Data;

/**
 * @author zyc
 * @version 1.0
 * 专门返回实体的json对象
 */
@Data
public class ReturnObject {
    /**
     * 处理成功成功获取失败的标记，1---成功，0---失败
     */
    private String code;

    /**
     * 提示信息
     */
    private String message;

    /**
     * 其他数据
     */
    private Object retData;
}
