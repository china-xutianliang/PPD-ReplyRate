package com.xtl.ebusiness;


import com.xtl.ecore.utils.SpringUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;


/**
 * 电商自动化程序
 */

@SpringBootApplication
@Import(SpringUtils.class)
@MapperScan("com.xtl.ebusiness.mapper")
public class AutomationApplication {


    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(AutomationApplication.class);
        application.run(args);
    }


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }



}
