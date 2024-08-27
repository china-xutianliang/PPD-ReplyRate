package com.xtl.ebusiness


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.ui.window.*
import com.xtl.ebusiness.system.windowBar.WindowBar
import org.springframework.boot.SpringApplication
import pdd.responserate.page.HomeScreen
import ui.theme.AppTheme



fun main() = application {
    // 启动 Spring Boot 应用程序
    SpringApplication.run(AutomationApplication::class.java)


    Window(
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(),
        title = WindowConfig.MAIN.title,
        icon = WindowConfig.MAIN.getIconPainter(),
        undecorated = true
    ) {
        AppTheme {
            Column {
                // 自定义窗口状态栏
                WindowDraggableArea {
                    WindowBar(
                        onMinimize = { window.isMinimized = true },
                        onClose = { exitApplication() },
                        showMoreButton = true
                    )
                }

                LoadingUtils.LoadingDialog()
                HomeScreen()

            }
        }
    }
}


