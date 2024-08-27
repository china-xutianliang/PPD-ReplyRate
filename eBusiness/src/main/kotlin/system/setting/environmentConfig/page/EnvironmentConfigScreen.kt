package com.xtl.ebusiness.system.setting.environmentConfig.page

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xtl.ebusiness.system.setting.environmentConfig.data.EnvironmentConfigDataKot
import com.xtl.ebusiness.system.setting.environmentConfig.funtion.querySystemConfig
import com.xtl.ebusiness.system.setting.environmentConfig.funtion.saveSystemConfig
import rememberToastUtils


@Preview
@Composable
fun EnvironmentConfigScreen() {

    var config by remember { mutableStateOf(EnvironmentConfigDataKot()) }

    val toastUtils = rememberToastUtils()

    // 使用 LaunchedEffect 来初始化 config
    LaunchedEffect(Unit) {
        config = querySystemConfig()
    }


    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {

        OutlinedTextField(
            value = config.simulatorUrl,
            onValueChange = { config = config.copy(simulatorUrl = it) },
            label = { Text("模拟器路径") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = config.kimiKey,
            onValueChange = {
                config = config.copy(kimiKey = it)
            },
            label = { Text("kimiKey") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = config.roleDesc,
            onValueChange = {
                config = config.copy(roleDesc = it)
            },
            label = { Text("角色描述") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        Button(
            onClick = {
                val result = saveSystemConfig(config)
                if (result) {
                    toastUtils.success("保存成功")
                } else {
                    toastUtils.error("保存失败")
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("保存")
        }

    }

}

