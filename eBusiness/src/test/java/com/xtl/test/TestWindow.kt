/*
package com.xtl.ebusiness

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.springframework.boot.SpringApplication
import pdd.responserate.page.HomeScreen
import ui.theme.AppTheme


fun main() = application {

    // 启动 Spring Boot 应用程序
    SpringApplication.run(AutomationApplication::class.java)

    // 设置窗口的初始大小
    //val windowState = WindowState(width = 2800.dp, height = 2600.dp)

    Window(
        onCloseRequest = ::exitApplication,
        title = "eDS",
        icon = painterResource("icons/testicon.png"), // 确保你的图标文件路径正确
        resizable = false, // 禁用窗口调整大小
        //state = windowState, // 使用定义的窗口状态
    ) {

        AppTheme {
            ToastUtils.ToastMessage()
            LoadingUtils.LoadingDialog()
            HomeScreen()
        }
    }
}
*/
