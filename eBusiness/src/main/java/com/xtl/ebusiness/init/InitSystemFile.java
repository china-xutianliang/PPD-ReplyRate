package com.xtl.ebusiness.init;

import com.xtl.ebusiness.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class InitSystemFile {

    @PostConstruct
    public void init() {

        appiumFile();

    }


    // Appium 相关文件
    private void appiumFile() {
        String resourcesDir = "snapshot/appium/node_modules/appium/node_modules/appium-uiautomator2-server/apks";
        String targetDir = "C:/snapshot/appium/node_modules/appium/node_modules/appium-uiautomator2-server/apks";

        String[] fileNames = new String[]{
                "appium-uiautomator2-server-debug-androidTest.apk",
                "appium-uiautomator2-server-debug-androidTest.apk.idsig",
                "appium-uiautomator2-server-v4.27.0.apk",
                "appium-uiautomator2-server-v4.27.0.apk.idsig"
        };

        for (String fileName : fileNames) {
            try {
                String resourcePath = resourcesDir + "/" + fileName;
                FileUtils.copyFileFromResources(resourcePath, targetDir);
                log.info("appiumFile 文件复制成功: {}", targetDir + "/" + fileName);
            } catch (Exception e) {
                log.error("appiumFile 复制文件 {} 失败: {}", fileName, e.getMessage(), e);
            }
        }
    }


}
