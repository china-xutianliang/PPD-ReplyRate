package com.xtl.ebusiness.service.system.impl;

import cn.hutool.json.JSONUtil;
import com.xtl.ebusiness.entity.EnvironmentConfig;
import com.xtl.ebusiness.mapper.CommonMapper;
import com.xtl.ebusiness.service.system.EnConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class EnConfigImpl implements EnConfig {

    public static EnvironmentConfig config;

    @Autowired
    CommonMapper commonMapper;

    @PostConstruct
    public void init() {
        querySystemConfig();
    }

    @Override
    public EnvironmentConfig querySystemConfig() {
        if (ObjectUtils.isEmpty(config)) {
            String configStr = commonMapper.querySystemConfig();
            config = JSONUtil.toBean(configStr, EnvironmentConfig.class);
        }
        return config;
    }

    @Override
    public Boolean saveSystemConfig(EnvironmentConfig newConfig) {
        config = newConfig;
        String configStr = JSONUtil.toJsonStr(newConfig);
        return commonMapper.updateSystemConfig(configStr);
    }
}
