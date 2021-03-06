package com.wang.db3;

/**
 * by wangrongjun on 2017/8/24.
 */

public class DefaultTypeLength {

    /**
     * int或Integer成员变量不指定总长度和小数长度时的默认总长度
     * 例如：int salary -> number(10,0)
     */
    public static final int ORACLE_INT_LENGTH = 10;
    /**
     * long或Long成员变量不指定总长度和小数长度时的默认总长度
     * 例如：long salary -> number(16,0)
     */
    public static final int ORACLE_LONG_LENGTH = 16;
    /**
     * float或Float成员变量不指定总长度和小数长度时的默认总长度
     * 例如：float salary -> number(10,1)
     */
    public static final int ORACLE_FLOAT_LENGTH = 10;
    public static final int ORACLE_FLOAT_DECIMAL_LENGTH = 1;
    /**
     * double或Double成员变量不指定总长度和小数长度时的默认总长度
     * 例如：float salary -> number(14,2)
     */
    public static final int ORACLE_DOUBLE_LENGTH = 14;
    public static final int ORACLE_DOUBLE_DECIMAL_LENGTH = 2;
    /**
     * String成员变量不指定长度时的默认长度
     * 例如：String name -> nvarchar2(100)
     */
    public static final int ORACLE_STRING_LENGTH = 100;

}
