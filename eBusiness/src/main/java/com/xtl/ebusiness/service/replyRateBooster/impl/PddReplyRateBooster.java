package com.xtl.ebusiness.service.replyRateBooster.impl;

import com.xtl.ebusiness.service.QuestionAnswerService;
import com.xtl.ebusiness.service.replyRateBooster.ReplyRateBooster;
import com.xtl.ebusiness.utils.AppiumUtils;
import com.xtl.ecore.enums.AppElementEnum;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PddReplyRateBooster extends ReplyRateBooster {

    @Autowired
    QuestionAnswerService questionAnswerService;

    public PddReplyRateBooster() {
        super("com.xunmeng.pinduoduo", "com.xunmeng.pinduoduo.ui.activity.MainFrameActivity","com.xunmeng.merchant","com.xunmeng.merchant.ui.MainFrameTabActivity");
    }

    @Override
    protected WebElement getChatIconBoxElement(AndroidDriver driver) {
        return AppiumUtils.uiautomatorBytext(driver, "text", "聊天",1);
    }

    @Override
    protected WebElement getChatSelectElement(AndroidDriver driver, String chatName) {
        String chatSelectStr = String.format("new UiSelector().%s(\"%s\")", "text", chatName);
        return AppiumUtils.waitForElementVisibility(driver, AppiumBy.androidUIAutomator(chatSelectStr), 0.1);
    }

    @Override
    protected WebElement getEditTextBox(AndroidDriver driver) {
        return AppiumUtils.waitForElementVisibility(driver, By.className(AppElementEnum.ANDROID_EDIT_TEXT.getCode()), 3);
    }

    @Override
    protected WebElement getSendButton(AndroidDriver driver) {
        // pdd只有输入的时候，才会显示这个发送按钮，所以先获取编辑进行输入再删除
        WebElement editTextBox = getEditTextBox(driver);
        editTextBox.sendKeys("在的");
        String sendMagStr = String.format("new UiSelector().%s(\"%s\")", "text", "发送");
        WebElement sendMagBox = AppiumUtils.waitForElementVisibility(driver, AppiumBy.androidUIAutomator(sendMagStr), 3);
        editTextBox.clear();
        return sendMagBox;
    }

    @Override
    protected boolean isBusinessReply(AndroidDriver driver) {
        List<WebElement> elements = driver.findElements(By.xpath("//android.support.v7.widget.RecyclerView[@resource-id=\"com.xunmeng.pinduoduo:id/pdd\"]/android.widget.LinearLayout"));
        WebElement webElement = elements.get(elements.size() - 1);
        List<WebElement> imageViews = webElement.findElements(By.xpath(".//android.widget.ImageView[@content-desc=\"商家头像\"]"));
        // 如果最后一条消息商家头像存在，则商家已回复
        return !imageViews.isEmpty();
    }

    @Override
    protected String isCustomerReply(AndroidDriver driver) {
        String answer = "";
        List<WebElement> elements = driver.findElements(By.xpath("//androidx.recyclerview.widget.RecyclerView[@resource-id=\"com.xunmeng.merchant:id/pdd\"]/android.widget.LinearLayout"));
        for (int i = elements.size() - 1; i >= 0; i--) {
            WebElement element = elements.get(i);
            List<WebElement> relativeLayouts = element.findElements(By.xpath(".//android.widget.RelativeLayout"));
            if (!relativeLayouts.isEmpty()) {
                WebElement relativeLayout = relativeLayouts.get(0); // 获取第一个 RelativeLayout
                List<WebElement> lastcontentWebElements = relativeLayout.findElements(By.xpath(".//android.widget.LinearLayout"));
                if(!lastcontentWebElements.isEmpty()){
                    WebElement lastcontentWebElement = lastcontentWebElements.get(0);
                    List<WebElement> textViews = lastcontentWebElement.findElements(By.xpath(".//android.widget.TextView"));
                    if (!textViews.isEmpty()) {
                        answer = questionAnswerService.getAnswer(textViews.get(0).getText()); //获取第一个 TextView 的文本
                        break;
                    }
                }
            }
        }
        return answer;
    }
}
