package com.xtl.ebusiness.service.simulator.impl;

import com.xtl.ebusiness.mapper.CommonMapper;
import com.xtl.ebusiness.service.simulator.Simulator;
import com.xtl.ebusiness.service.system.impl.EnConfigImpl;
import com.xtl.ecore.exception.BusinessException;
import com.xtl.ecore.utils.CmdUtils;
import com.xtl.ecore.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
public class SimulatorImpl implements Simulator {

    private final ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();

    SimulatorImpl() {
        taskScheduler.initialize();
    }

    @Autowired
    CommonMapper commonMapper;


    @Override
    public String startSimulatorByDeviceName(int simulatorType, String deviceName) throws BusinessException {

        String path = EnConfigImpl.config.getSimulatorUrl();
        if (StringUtils.isEmpty(path)) {
            throw new BusinessException("模拟器路径未配置" );
        }

        try {
            String ldconsolePath = Paths.get(path, "ldconsole.exe").toString();
            String adbPath = Paths.get(path, "adb.exe").toString();

            // 检查设备是否已运行，如果未运行，则自动拉起
            if (!isDeviceRunning(ldconsolePath, deviceName)) {
                try {
                    String launch = CmdUtils.executeCommand(ldconsolePath, "launch", "--name", deviceName);
                    System.out.println(launch);
                }catch (Exception e){
                    log.error(e.getMessage());
                    throw new BusinessException("不存在该模拟器名称:"+deviceName);
                }
            }

            // 解锁屏幕
            //CmdUtils.executeCommand(ldconsolePath, "adb", "--name", deviceName, "--command", "shell input keyevent 82");

            // 查询当前模拟器设备ID
            String deviceId = getDeviceIdByName(ldconsolePath, deviceName);

            // 等待成功拉起
            CountDownLatch latch = new CountDownLatch(1);
            int maxWaitTime = 180; // 最大等待时间为180秒（3分钟）
            int interval = 5;      // 每次检查的间隔为5秒

            ScheduledFuture<?> future = taskScheduler.scheduleAtFixedRate(() -> {
                String s = CmdUtils.executeCommand(adbPath, "-s", deviceName, "shell", "getprop", "init.svc.bootanim");
                if (StringUtils.isEmpty(CmdUtils.executeCommand(adbPath, "-s", deviceId, "shell", "getprop", "init.svc.bootanim"))) {
                    latch.countDown(); // 设备成功启动，解除等待
                }
            }, interval * 1000L);

            // 等待设备启动完成或超时
            boolean started = latch.await(maxWaitTime, TimeUnit.SECONDS);
            future.cancel(true); // 无论启动成功或超时，都停止调度

            if (!started) {
                log.error("设备:{}   启动超时",deviceName);
                throw new BusinessException("设备启动超时");
            }
            log.info("已成功拉起设备：{}",deviceId);

            return deviceId;


        } catch (Exception e) {
            if (e instanceof BusinessException be) {
                log.warn("serverWebInputException：{}  {}",be.getCode() , be.getMessage());
                throw be;
            }
            log.error("启动模拟器设备失败 simulatorType：{} deviceName：{} msg:{} ", simulatorType, deviceName, e.getMessage(), e);
            throw new BusinessException("启动模拟器设备失败");
        }
    }

    @Override
    public String getDeviceIdByName(String ldconsolePath, String deviceName) {

        try {
            String output = CmdUtils.executeCommand(ldconsolePath, "list2");
            List<String> lines = Arrays.asList(output.split("\n"));
            for (String line : lines) {
                String[] fields = line.split(",");
                if (fields.length > 1 && deviceName.equals(fields[1].trim())) {
                    String deviceId = fields[0].trim();
                    int port = 5554 + Integer.parseInt(deviceId) * 2;
                    return "emulator-" + port;
                }
            }
        } catch (Exception e) {
            log.error("查询模拟器设备ID或计算端口号失败 deviceName：{} msg:{} ", deviceName, e.getMessage(), e);
            throw new BusinessException("查询模拟器设备ID或计算端口号失败");
        }
        return null;
    }


    private boolean isDeviceRunning(String simulatorPath, String deviceName){
        String output = CmdUtils.executeCommand(simulatorPath, "isrunning", "--name", deviceName);
        return "running".equalsIgnoreCase(output.trim());
    }


}
