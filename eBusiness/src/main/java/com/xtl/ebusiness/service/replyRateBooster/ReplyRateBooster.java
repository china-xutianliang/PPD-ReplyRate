package com.xtl.ebusiness.service.replyRateBooster;

import com.xtl.eSdk.kimi.api.KimiApi;
import com.xtl.ebusiness.service.QuestionAnswerService;
import com.xtl.ebusiness.service.system.impl.EnConfigImpl;
import com.xtl.ebusiness.utils.AppiumUtils;
import com.xtl.ecore.entity.CommonResult;
import com.xtl.ecore.entity.EdsTimer;
import com.xtl.ecore.exception.BusinessException;
import com.xtl.ecore.utils.ResultUtils;
import com.xtl.ecore.utils.SpringUtils;
import com.xtl.ecore.utils.StringUtils;
import io.appium.java_client.android.AndroidDriver;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicReference;

import static com.xtl.ebusiness.utils.TaskUtils.edsTimerMap;

/**
 * 自动刷高商家回复率
 */
@Slf4j
public abstract class ReplyRateBooster {

    // 客户应用包名称
    protected String userAppPackage;
    // 客户应用包启动入口
    protected String userAppActivity;

    // 客户应用包名称
    protected String businessAppPackage;
    // 客户应用包启动入口
    protected String businessAppActivity;

    QuestionAnswerService questionAnswerService = SpringUtils.getBean(QuestionAnswerService.class);

    protected ReplyRateBooster(String userAppPackage, String userAppActivity,String businessAppPackage, String businessAppActivity) {
        this.userAppPackage = userAppPackage;
        this.userAppActivity = userAppActivity;
        this.businessAppPackage = businessAppPackage;
        this.businessAppActivity = businessAppActivity;
    }


    /**
     * 公共方法，组装并执行自动聊天任务
     *
     * @param type                  类型 0商家  1顾客
     * @param deviceID              设备ID
     * @param chatName              聊天对象名称
     * @return CommonResult 任务结果
     */
    public CommonResult<?> startReplyRateTask(int type, String deviceID, String chatName) {
        // 如果type=0商家,则需要这个角色
        String roleDesc = EnConfigImpl.config.getRoleDesc();
        String kimiKey = EnConfigImpl.config.getKimiKey();
        if(type==0 && StringUtils.isEmpty(roleDesc)){
            throw new BusinessException("AI角色定位未输入!");
        }


        AndroidDriver driver = null;
        EdsTimer timer = null;
        String taskId;
        AtomicReference<String> question = new AtomicReference<>();
        try {
            // 启动App
            driver = startApp(type,deviceID);

            // 拉起聊天
            try {
                WebElement chatIconElement = getChatIconBoxElement(driver);
                chatIconElement.click();
            }catch (Exception e){
                throw  new BusinessException("未找到聊天图标");
            }

            // 选择聊天对象
            try {
                WebElement chatSelectElement = getChatSelectElement(driver, chatName);
                chatSelectElement.click();
            }catch (Exception e){
                throw  new BusinessException("未找聊天对象:" + chatName);
            }


            // 定位文本框
            WebElement editTextBox = getEditTextBox(driver);

            // 定位发送按钮
            WebElement sendButton = getSendButton(driver);

            // 启动定时任务
            final AndroidDriver finalDriver = driver;
            timer = new EdsTimer();
            edsTimerMap.put(timer.getId(),timer);


            TimerTask task;
            switch (type) {
                case 0:
                    task = new TimerTask() {
                        @Override
                        public void run() {
                            // 客户是否已回复
                            String answer = isCustomerReply(finalDriver);
                            if (StringUtils.isNotEmpty(answer)) {
                                String answerChange = KimiApi.kimiAiChat(roleDesc,kimiKey,answer);
                                editTextBox.sendKeys(answerChange);
                                sendButton.click();
                            }
                        }
                    };
                    timer.schedule(task, 0, 10000); // 每隔10秒执行一次任务
                    break;
                case 1:
                    task = new TimerTask() {
                        @Override
                        public void run() {
                            // 商家回复,则继续问
                            if (isBusinessReply(finalDriver)) {
                                question.set(questionAnswerService.getRandomQuestion(question.get()));
                                editTextBox.sendKeys(question.get());
                                sendButton.click();
                            }
                        }
                    };
                    timer.schedule(task, 0, 30000); // 每隔30秒执行一次任务
                    break;
                default:
                    throw new IllegalArgumentException("Invalid task type: " + type);
            }

        } catch (Exception e) {
            if (timer != null) {
                timer.cancel();
            }

            if (driver != null) {
                driver.quit();
            }
            if (e instanceof BusinessException be) {
                log.warn("startReplyRateTask：{}  {}",be.getCode() , be.getMessage());
                throw be;
            }
            throw new BusinessException("拉起APP失败,如果已拉起,请先关闭重试");
        }
        return ResultUtils.success(timer.getId());
    }




    /**
     * 启动安卓应用(目前默认安卓配置)
     *
     * @param deviceID 设备ID
     * @return AndroidDriver 实例
     */
    public AndroidDriver startApp(int type,String deviceID) {
        String automationName = "UiAutomator2";
        String platformName = "Android";

        String appPackage = (type == 0) ? businessAppPackage : userAppPackage;
        String appActivity = (type == 0) ? businessAppActivity : userAppActivity;


        return AppiumUtils.startApp(deviceID, automationName, platformName, appPackage, appActivity);
    }

    /**
     * 获取聊天图标按钮页面元素
     *
     * @param driver AndroidDriver 实例
     * @return WebElement
     */
    protected abstract WebElement getChatIconBoxElement(AndroidDriver driver);

    /**
     * 获取聊天对象元素
     *
     * @param driver AndroidDriver 实例
     * @param chatName 聊天对象名称
     * @return 聊天对象元素 WebElement
     */
    protected abstract WebElement getChatSelectElement(AndroidDriver driver, String chatName);

    /**
     * 获取文本框元素
     *
     * @param driver AndroidDriver 实例
     * @return 文本框元素 WebElement
     */
    protected abstract WebElement getEditTextBox(AndroidDriver driver);

    /**
     * 获取发送按钮元素
     *
     * @param driver AndroidDriver 实例
     * @return 发送按钮元素 WebElement
     */
    protected abstract WebElement getSendButton(AndroidDriver driver);

    /**
     * 判断商家是否已回复
     *
     * @param driver AndroidDriver 实例
     * @return boolean
     */
    protected abstract boolean isBusinessReply(AndroidDriver driver);

    /**
     * 判断客户是否已回复，并且获取问题对应的答案
     *
     * @param driver AndroidDriver 实例
     * @return String 客户回复的答案
     */
    protected abstract String isCustomerReply(AndroidDriver driver);

}
