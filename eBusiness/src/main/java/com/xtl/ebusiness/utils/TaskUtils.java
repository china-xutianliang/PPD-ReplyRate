package com.xtl.ebusiness.utils;

import com.xtl.ecore.entity.CommonResult;
import com.xtl.ecore.entity.EdsTimer;
import com.xtl.ecore.utils.ResultUtils;

import java.util.*;

public class TaskUtils {

    // 任务集合
    public static Map<String, EdsTimer> edsTimerMap = new HashMap<>();

    public static CommonResult cancelTask(String taskId) {
        EdsTimer timer = edsTimerMap.get(taskId);
        if (timer != null) {
            timer.cancel();
            edsTimerMap.remove(taskId);
        }
        return ResultUtils.success();
    }
}
