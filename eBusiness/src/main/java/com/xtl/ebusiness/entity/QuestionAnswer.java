package com.xtl.ebusiness.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("QUESTION_ANSWER")
public class QuestionAnswer {

    @TableId
    public String question; // 问题
    public String answer; // 答案

}
