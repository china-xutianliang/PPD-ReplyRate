package com.xtl.ebusiness.service.system;

import com.xtl.ebusiness.entity.EnvironmentConfig;

public interface EnConfig {

    // 查询环境变量
    EnvironmentConfig querySystemConfig();

    // 保存环境变量
    Boolean saveSystemConfig(EnvironmentConfig newConfig);

}
