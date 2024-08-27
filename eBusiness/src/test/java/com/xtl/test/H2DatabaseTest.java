package com.xtl.test;

import com.xtl.ebusiness.AutomationApplication;
import com.xtl.ebusiness.entity.QuestionAnswer;
import com.xtl.ebusiness.mapper.QuestionAnswerMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@SpringBootTest(classes = AutomationApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class H2DatabaseTest {

    @Autowired
    private QuestionAnswerMapper questionAnswerMapper;

    @Test
    @Rollback(false)
    public void testH2Database() {
        System.out.println("--- Testing H2 Database ---");
        // 添加测试数据
        QuestionAnswer qa1 = new QuestionAnswer();
        qa1.setQuestion("Test Question 1");
        qa1.setAnswer("Test Answer 1");
        questionAnswerMapper.insert(qa1);

        QuestionAnswer qa2 = new QuestionAnswer();
        qa2.setQuestion("Test Question 2");
        qa2.setAnswer("Test Answer 2");
        questionAnswerMapper.insert(qa2);

        // 查询所有问答
        List<QuestionAnswer> questionAnswers = questionAnswerMapper.selectList(null);
        questionAnswers.forEach(qa ->
                System.out.println("问答："+qa.getQuestion()+" ->" +qa.getAnswer() )
        );


        System.out.println("--- H2 Database Test Completed ---");
    }
}
