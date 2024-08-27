package com.xtl.ebusiness.utils;

import com.xtl.ebusiness.config.InitBean;
import com.xtl.ecore.exception.BusinessException;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

@Slf4j
public class AppiumUtils {


    /**
     * 拉起APP应用
     *
     * @param deviceID       虚拟设备名称
     * @param automationName 自动化引擎
     * @param platformName   安卓/苹果
     * @param appPackage     App包名称
     * @param appActivity    入口页
     * @return
     */
    public static AndroidDriver startApp(String deviceID, String automationName, String platformName, String appPackage, String appActivity) {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("udid", deviceID);
        caps.setCapability("automationName", automationName);
        caps.setCapability("platformName", platformName);
        caps.setCapability("appPackage", appPackage);
        caps.setCapability("appActivity", appActivity);
        caps.setCapability("noReset", true); // 保留应用状态
        caps.setCapability("dontStopAppOnReset", true); // 会话结束后不停止应用
        caps.setCapability("unicodeKeyboard", true); // 支持中文输入
        caps.setCapability("resetKeyboard", true); // 重置键盘

        return new AndroidDriver(InitBean.url, caps);
    }

    /*public static AndroidDriver startApp(String deviceID, String automationName, String platformName, String appPackage, String appActivity) {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("udid", deviceID);
        caps.setCapability("automationName", automationName);
        caps.setCapability("platformName", platformName);
        caps.setCapability("appPackage", appPackage);
        caps.setCapability("appActivity", appActivity);
        caps.setCapability("noReset", true); // 保留应用状态
        caps.setCapability("dontStopAppOnReset", true); // 会话结束后不停止应用
        caps.setCapability("unicodeKeyboard", true); // 支持中文输入
        caps.setCapability("resetKeyboard", true); // 重置键盘

        AndroidDriver driver = null;
        try {
            // 初始化AndroidDriver
            driver = new AndroidDriver(InitBean.url, caps);

            // 等待应用启动，使用WebDriverWait来等待应用到达预期的Activity或元素
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // 验证应用已成功启动 (根据Activity名称)
            try {
                String currentActivity = driver.currentActivity();
                if (currentActivity.equals(appActivity)) {
                    System.out.println("应用已成功启动并在预期的 Activity 上: " + currentActivity);
                } else {
                    System.out.println("应用启动失败或在意外的 Activity 上: " + currentActivity);
                }
            } catch (WebDriverException e) {
                System.out.println("无法获取当前 Activity，可能应用未启动或启动失败。");
            }

            // 或者，你可以通过检查关键元素来验证应用已启动
            // 例如，等待应用的某个首页按钮可见
            // wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.example.yourapp:id/homeButton")));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return driver;
    }*/


    /**
     * 等待登录校验
     *
     * @param driver
     * @param timeoutInMinutes 检验超时时间
     * @return
     */

    public static WebElement waitForElementVisibility(AndroidDriver driver, By selectElement, double timeoutInMinutes) {
        int timeoutInSeconds = (int) (timeoutInMinutes * 60); // 将分钟转换为秒
        try {
            WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(selectElement));
            return element;
        } catch (Exception e) {
            log.error("元素未找到:{}", e.getMessage());
            throw new BusinessException("元素未找到:{}" + e.getMessage());
        }
    }

    public static boolean waitForLogin(AndroidDriver driver, String type, String loginContent, int timeoutInMinutes) {
        int timeoutInSeconds = timeoutInMinutes * 60; // 将分钟转换为秒
        try {
            WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
            String loginSelectStr = String.format("new UiSelector().%s(\"%s\")", type, loginContent);
            By loginSelect = AppiumBy.androidUIAutomator(loginSelectStr);
            WebElement loginElement = wait.until(ExpectedConditions.visibilityOfElementLocated(loginSelect));
            return loginElement != null;
        } catch (Exception e) {
            log.error("登录失败,请重新登录");
            throw new BusinessException("登录失败,请重新登录");
        }
    }


    /**
     * 获取 RecyclerView 元素下所有子节点的数量
     *
     * @param driver              AndroidDriver 实例
     * @param recyclerViewLocator RecyclerView 元素的定位器
     * @return RecyclerView 元素下所有子节点的数量，如果定位失败则返回 -1
     */
    public static int getChildCount(AndroidDriver driver, By recyclerViewLocator) {
        try {
            // 使用 findElement 定位 RecyclerView 元素
            WebElement recyclerView = driver.findElement(recyclerViewLocator);
            // 获取父节点下的所有子节点
            List<WebElement> childElements = recyclerView.findElements(By.xpath("./*"));
            // 返回子节点数量
            return childElements.size();
        } catch (Exception e) {
            // 如果找不到 RecyclerView 元素，返回 -1 表示失败
            return -1;
        }
    }


    /******************         -android uiautomator(docs) 获取元素方法    *****************/
    /**
     * @param driver
     * @param type              类型
     * @param content           内容
     * @param timeoutInMinutes: 等待的超时时间，以秒为单位。
     * @return
     */
    public static WebElement uiautomatorBytext(AndroidDriver driver, String type, String content, int timeoutInMinutes) {
        String uiAutomatorStr = String.format("new UiSelector().%s(\"%s\")", type, content);
        int timeoutInSeconds = timeoutInMinutes * 60;
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        try {
            return wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.androidUIAutomator(uiAutomatorStr)));
        } catch (Exception e) {
            log.error("在 {} 分钟内未找到元素: {}，内容: {}", timeoutInMinutes, type, content, e);
            throw new BusinessException("未找到元素: "+content);
        }
    }


    /**************************         xpath 获取元素方法           ***************************/

    /**
     * 根据元素的资源ID类型和文本定位
     *
     * @param driver
     * @param resourceId 资源ID
     * @param text       文本
     * @return
     */
    public static WebElement xpathByResourceIdAndText(AndroidDriver driver, String resourceId, String text) {
        String xpath = String.format("//android.widget.TextView[@resource-id='%s' and @text='%s']", resourceId, text);
        return driver.findElement(By.xpath(xpath));
    }


    /**
     * 根据元素的内容描述来定位元素。
     *
     * @param contentDesc 元素的内容描述
     * @return 定位到的元素对象
     * 比如:xpath = //android.widget.TextView[@content-desc="返回"]  前端上实际展示的是图标
     */
    public static WebElement xpathByContentDesc(AndroidDriver driver, String contentDesc) {
        return driver.findElement(By.xpath("//*[@content-desc='" + contentDesc + "']"));
    }


}
