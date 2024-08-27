package com.xtl.ebusiness.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xtl.ebusiness.entity.QuestionAnswer;

public interface QuestionAnswerService extends IService<QuestionAnswer> {


    /**
     * 获取随机问题(排除当前问题)
     * @param question 问题
     * @return
     */
    String getAnswer(String question);

    /**
     * 获取随机问题
     * @param question  排除当前传过来的问题
     * @return
     */
    String getRandomQuestion(String question);

}