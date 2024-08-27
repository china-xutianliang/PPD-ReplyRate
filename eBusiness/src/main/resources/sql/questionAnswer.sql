-- 自动回复题库表
CREATE TABLE IF NOT EXISTS question_answer (
    question VARCHAR(255) PRIMARY KEY,  -- 主键，问题
    answer VARCHAR(255) NOT NULL        -- 答案
);
-- 插入模拟数据
/*INSERT INTO QUESTION_ANSWER (question, answer) VALUES
('你好', '你好，有什么我可以帮助您的吗？'),
('这个怎么卖', '我可以回答您的问题，帮助解决问题。'),
('谢谢', '不客气，这是我的职责。'),
('能优惠一点吗', '再见，祝您有美好的一天！'),
('包运费险吗', '再见，祝您有美好的一天！'),
('多久能到广州', '再见，祝您有美好的一天！')*/

