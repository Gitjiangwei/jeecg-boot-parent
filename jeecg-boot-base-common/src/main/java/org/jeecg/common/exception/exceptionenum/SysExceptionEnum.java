package org.jeecg.common.exception.exceptionenum;

/**
 * 异常常量
 *
 * @author 姜伟
 * @date 2020/7/24
 */
public enum  SysExceptionEnum{
    /**
     * 系统异常
     */
    SYS_EXCEPTION("S0001","系统发送异常"),

    /**
     * 集合拷贝异常
     */
    BEANS_COPY_EXCEPTION("S0002","集合拷贝失败"),

    /**
     * 其它异常
     */
    EXTERNAL_SYS_EXCEPTION("S0003","外部系统发送异常")
    ;

    private final String exceptionCode;
    private final String exceptionMsg;


    SysExceptionEnum(String exceptionCode, String exceptionMsg){
        this.exceptionCode = exceptionCode;
        this.exceptionMsg = exceptionMsg;
    }

    public String getExceptionCode() {
        return this.exceptionCode;
    }

    public String getExceptionMsg() {
        return this.exceptionMsg;
    }


}