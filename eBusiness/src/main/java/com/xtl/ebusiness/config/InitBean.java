package com.xtl.ebusiness.config;

import com.xtl.ecore.utils.CmdUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 初始化基础对象类
 */
@Slf4j
@Component
@Data
public class InitBean implements InitializingBean {

    public static URL url;

    // appium服务端口
    @Value("${appiumService.port}")
    private int appiumServicePort;

    public static String projectDir = Paths.get(System.getProperty("user.dir"), "app", "resources").toString();

    /**
     * 启动appium服务
     */
    @Override
    public void afterPropertiesSet() throws Exception {

        log.info("Initializing Appium service on port: " + appiumServicePort);
        if (isPortInUse(appiumServicePort)) {
            log.info("Port " + appiumServicePort + " is in use. Stopping the process using the port.");
            forceStopPort(appiumServicePort);
            Thread.sleep(1000); // 等待1秒
        }

        // 启动appium
        Path appiumExecutablePath = Paths.get(projectDir, "appium.exe");
        CmdUtils.executeCommandAsync(appiumExecutablePath.toString(), String.valueOf(appiumServicePort));
        // 设置appium 访问url

        url = new URL("http", "localhost", appiumServicePort, "/wd/hub");
        log.info("Appium server URL: " + url);

    }

    /**
     * 检查端口是否被占用
     *
     * @param port 端口号
     * @return 是否被占用
     */
    private boolean isPortInUse(int port) {
        try {
            String command = "cmd.exe /c netstat -ano | findstr :" + port;
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("LISTENING")) {
                    return true;
                }
            }
        } catch (Exception e) {
            log.error("Error checking port usage: " + e.getMessage(), e);
        }
        return false;
    }

    /**
     * 强制停止使用该端口的进程
     *
     * @param port 端口号
     */
    private void forceStopPort(int port) {
        try {
            String command = "cmd.exe /c netstat -ano | findstr :" + port;
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("LISTENING")) {
                    String[] parts = line.trim().split("\\s+");
                    String pid = parts[parts.length - 1];
                    // 强制结束该进程
                    Runtime.getRuntime().exec("taskkill /F /PID " + pid);
                    log.info("Process with PID " + pid + " on port " + port + " has been terminated.");
                }
            }
        } catch (Exception e) {
            log.error("Error stopping the process on port " + port + ": " + e.getMessage(), e);
        }
    }
}
