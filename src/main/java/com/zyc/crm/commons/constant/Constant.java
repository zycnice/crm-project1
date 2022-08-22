package com.zyc.crm.commons.constant;

/**
 * 常量类，写死数据做规范
 * @author zyc
 * @version 1.0
 */
public class Constant {

    //保存ReturnObject类中的Code值
    /**
     *1表示成功
     * 0表示失败
     */
    public static final String RETURN_OBJECT_CODE_SUCCESS="1";
    public static final String RETURN_OBJECT_CODE_FAIL="0";

    /**
     * 保存当前用户的key（session等作用域）
     */
    public static final String SESSION_USER = "sessionUser";

    /**
     * 备注修改标记 0 表示每修改过
     */
    public static final String REMARK_EDIT_FLAG_NO_EDITED="0";

    /**
     * 备注修改标记 1 表示每修改过
     */
    public static final String REMARK_EDIT_FLAG_YES_EDITED="1";
}
