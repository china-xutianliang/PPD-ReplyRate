# 1. 模拟器相关 simulator



# 2. 商家刷高自动回复率模块  ReplyRateBooster
已封装，对应各个平台，只需按步骤填充对应代码即可
示例:
- 继承ReplyRateBooster，构造填充对应包名、应用入口
public PddReplyRateBooster() {
super("com.xunmeng.pinduoduo", "com.xunmeng.pinduoduo.ui.activity.MainFrameActivity");
} 
- 实现 获取聊天图标按钮页面元素抽象方法  getChatIconBoxElement 
- 实现 获取聊天对象元素  getChatSelectElement
- 实现 获取文本输入框元素  getEditTextBox
- 实现 获取发送按钮元素  getSendButton
- 实现 判断商家是否已回复  isBusinessReply
- 实现 判断客户是否已回复，并且获取问题对应的答案  isCustomerReply