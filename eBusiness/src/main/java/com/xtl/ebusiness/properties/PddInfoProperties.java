package com.xtl.ebusiness.properties;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author 许天良
 * @date 2024/5/4
 * 拼多多配置信息
 */

@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "pingduoduoinfo")
public class PddInfoProperties {

    private String appCustomerPackage;
    private String appCustomerActivity;
    private String appBusinessPackage;
    private String appBusinessActivity;


}
