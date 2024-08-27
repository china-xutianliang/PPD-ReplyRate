package com.xtl.ebusiness.system.windowBar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xtl.ebusiness.system.setting.windowSetting
import icons.Minimize

/**
 * 窗口栏bar
 * @param onMinimize 回调函数，在最小化按钮被点击时触发
 * @param onClose 回调函数，在关闭按钮被点击时触发
 * @param showMoreButton 是否显示更多按钮
 */
@Composable
fun WindowBar(
    onMinimize: () -> Unit,
    onClose: () -> Unit,
    showMoreButton: Boolean = false
) {

    var settingShowState by remember { mutableStateOf(false) }
    if(showMoreButton && settingShowState ){
        // 将settingShowState传递过去，由windowSetting窗口调用，重新设置隐藏
        windowSetting(onClose = { settingShowState = false })
    }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .height(35.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.weight(1f))

        if (showMoreButton) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "MoreVert",
                modifier = Modifier
                    .clickable { settingShowState = true}
                    .padding(8.dp),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        Icon(
            imageVector = Icons.Default.Minimize,
            contentDescription = "Minimize",
            modifier = Modifier
                .clickable(onClick = onMinimize)
                .padding(8.dp),
            tint = MaterialTheme.colorScheme.onPrimary
        )
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Close",
            modifier = Modifier
                .clickable(onClick = onClose)
                .padding(8.dp),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}
