package com.xtl.ebusiness.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_public_paramvalue")
public class PublicParamValue {

    private String paramname;
    private String paramvalue;
    private String module;
    private String memo;
}
