package com.xtl.ebusiness.system.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QuestionBank
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.window.Window
import com.xtl.ebusiness.system.setting.environmentConfig.page.EnvironmentConfigScreen
import com.xtl.ebusiness.system.setting.questionAnswer.page.QuestionAnswerScreen
import com.xtl.ebusiness.system.windowBar.WindowBar


enum class NavigationItem(val title: String, val icon: ImageVector) {
    QUESTION_BANK("问题库", Icons.Filled.QuestionBank),
    VM_ENV_SETUP("模拟器路径", Icons.Filled.Settings)
}

@Composable
fun SettingPage() {
    var selectedItem by remember { mutableStateOf(NavigationItem.QUESTION_BANK) }

    Row(modifier = Modifier.fillMaxSize()) {
        NavigationRail(
            modifier = Modifier.fillMaxHeight(),
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                NavigationItem.values().forEach { item ->
                    NavigationRailItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = selectedItem == item,
                        onClick = { selectedItem = item }
                    )
                }
            }
        }
        ContentPanel(selectedItem = selectedItem)
    }
}

@Composable
fun ContentPanel(selectedItem: NavigationItem) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        when (selectedItem) {
            NavigationItem.QUESTION_BANK -> QuestionAnswerScreen()
            NavigationItem.VM_ENV_SETUP -> EnvironmentConfigScreen()
        }
    }
}

// 窗口
@Composable
fun windowSetting(onClose: () -> Unit) {
    Window(
        onCloseRequest = onClose,
        title = WindowConfig.SYSTEM.title,
        icon = WindowConfig.SYSTEM.getIconPainter(),
        undecorated = true
    ) {
        Column {
            WindowDraggableArea {
                WindowBar(
                    onMinimize = { window.isMinimized = true },
                    onClose = onClose
                )
            }
            SettingPage()
        }
    }
}