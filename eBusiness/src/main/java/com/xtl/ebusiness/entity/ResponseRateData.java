package com.xtl.ebusiness.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("RESPONSE_RATE_DATA")
public class ResponseRateData {

    /**
     * 主键，自动递增
     */
    @TableId
    public String id;

    /**
     * 模拟器类型 0雷电 1闪现 2机电 等等
     */
    public int simulatorType;

    /**
     * 模拟器启动名称
     */
    public String simulatorName;

    /**
     * 类型，例如设备或聊天对象（0商家 1顾客）
     */
    public int roleType;

    /**
     * 聊天对象名称
     */
    public String chatName;

}
