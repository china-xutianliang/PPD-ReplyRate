package com.xtl.ebusiness.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xtl.ebusiness.entity.QuestionAnswer;
import com.xtl.ebusiness.mapper.QuestionAnswerMapper;
import com.xtl.ebusiness.service.QuestionAnswerService;
import com.xtl.ecore.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class QuestionAnswerServiceImpl extends ServiceImpl<QuestionAnswerMapper, QuestionAnswer> implements QuestionAnswerService {

    @Autowired
    private QuestionAnswerMapper questionAnswerMapper;

    // 使用ConcurrentHashMap缓存题目和答案
    private final ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();

    /**
     * 初始化缓存
     */
    private void initializeCache() {
        // 查询所有问题答案对
        List<QuestionAnswer> allQuestionAnswers = questionAnswerMapper.selectList(new QueryWrapper<>());

        // 将所有问题答案对放入缓存
        for (QuestionAnswer questionAnswer : allQuestionAnswers) {
            cache.put(questionAnswer.getQuestion(), questionAnswer.getAnswer());
        }
    }

    /**
     * 查找题目对应的答案
     * @param question 问题
     * @return
     */
    @Override
    public String getAnswer(String question) {
        // 如果缓存为空，则进行初始化
        if (cache.isEmpty()) {
            initializeCache();
        }

        return cache.get(question);
    }

    /**
     * 随机获取一个题目（排除传入的题目）
     *
     * @param excludedQuestion 需要排除的题目
     * @return 随机获取的题目
     */
    @Override
    public String getRandomQuestion(String excludedQuestion) {
        // 如果缓存为空，则进行初始化
        if (cache.isEmpty()) {
            initializeCache();
        }

        // 获取缓存中的所有题目
        List<String> questions = cache.keySet().stream()
                .filter(q -> !q.equals(excludedQuestion))
                .toList();

        // 如果过滤后没有剩余题目，返回null或其他适当的值
        if (questions.isEmpty()) {
            throw new BusinessException("当前问题答案库为空，请先进行添加");
        }

        // 从过滤后的题目中随机选择一个
        int randomIndex = ThreadLocalRandom.current().nextInt(questions.size());
        return questions.get(randomIndex);
    }

}
