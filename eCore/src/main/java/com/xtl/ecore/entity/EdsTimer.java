package com.xtl.ecore.entity;

import cn.hutool.core.util.IdUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Timer;
import java.util.TimerTask;

@Data
@Slf4j
public class EdsTimer {

    private String id;
    private Timer timer;

    public EdsTimer() {
        this.id = IdUtil.simpleUUID();
        this.timer = new Timer();
    }

    public void schedule(TimerTask task, long delay, long period) {
        timer.schedule(task, delay, period);
    }

    public void cancel() {
        timer.cancel();
        log.info("任务ID已经关闭：{}",id);
    }

}
