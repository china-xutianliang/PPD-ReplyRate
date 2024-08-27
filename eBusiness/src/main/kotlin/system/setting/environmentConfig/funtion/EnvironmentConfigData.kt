package com.xtl.ebusiness.system.setting.environmentConfig.funtion

import cn.hutool.json.JSONUtil
import com.xtl.ebusiness.entity.EnvironmentConfig
import com.xtl.ebusiness.service.system.impl.EnConfigImpl
import com.xtl.ebusiness.system.setting.environmentConfig.data.EnvironmentConfigDataKot
import com.xtl.ecore.utils.SpringUtils
import org.modelmapper.ModelMapper
import org.modelmapper.convention.MatchingStrategies
import utils.BeanUtils

val modelMapper = ModelMapper()



fun main() {
    val modelMapper = ModelMapper().apply {
        configuration.matchingStrategy = MatchingStrategies.STRICT
    }

    val environmentConfig = EnvironmentConfig().apply {
        simulatorUrl = "test_url"
        kimiKey = "test_key"
        roleDesc = "test_desc"
    }

    val environmentConfigDataKot = modelMapper.map(environmentConfig, EnvironmentConfigDataKot::class.java)

    println(environmentConfigDataKot.simulatorUrl)  // 应该输出 "test_url"
    println(environmentConfigDataKot.kimiKey)       // 应该输出 "test_key"
    println(environmentConfigDataKot.roleDesc)      // 应该输出 "test_desc"
}
/**
 * 查询系统环境配置
 */
fun querySystemConfig(): EnvironmentConfigDataKot {
    val enConfigImpl = SpringUtils.getBean(EnConfigImpl::class.java)
    val environmentConfig = enConfigImpl.querySystemConfig()
    val target = EnvironmentConfigDataKot()
    BeanUtils.copyProperties(environmentConfig, target)
    return target
}

/**
 * 更新系统环境变量
 */
fun saveSystemConfig(newConfig: EnvironmentConfigDataKot): Boolean {
    val enConfigImpl = SpringUtils.getBean(EnConfigImpl::class.java)
    val vo = EnvironmentConfig()
    BeanUtils.copyProperties(newConfig, vo)
    return enConfigImpl.saveSystemConfig(vo)
}