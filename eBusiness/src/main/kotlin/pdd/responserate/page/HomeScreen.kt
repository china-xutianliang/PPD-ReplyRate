package pdd.responserate.page

import DraggableAddDeviceDialogContent
import TableFormObj
import TableHeadObj
import ToastUtils
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import com.xtl.ebusiness.pdd.responserate.funtion.*
import pdd.responserate.data.*
import pdd.responserate.data.ResponseRateTypeOptions
import rememberToastUtils
import ui.theme.AppTheme

@Composable
fun HomeScreen() {
    val toastUtils = rememberToastUtils()
    var showAddDeviceDialog by remember { mutableStateOf(false) }
    var refreshData by remember { mutableStateOf<(() -> Unit)?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Spacer(modifier = Modifier.height(16.dp))

        val tableHead = mapOf(
            "simulatorType" to TableHeadObj(description = "模拟器类型", width = 120.dp),
            "simulatorName" to TableHeadObj(description = "模拟器启动名称", width = 160.dp),
            "roleType" to TableHeadObj(description = "角色类型", width = 120.dp),
            "chatName" to TableHeadObj(description = "聊天对象名称", width = 200.dp),
            "switch" to TableHeadObj(description = "开关")
        )

        val tableForm = mapOf(
            "simulatorType" to TableFormObj(type = FormFieldType.Dropdown(options = SimulatorTypeOptions)),
            "simulatorName" to TableFormObj(type = FormFieldType.EditableText),
            "roleType" to TableFormObj(type = FormFieldType.Dropdown(options = ResponseRateTypeOptions)),
            "chatName" to TableFormObj(type = FormFieldType.EditableText),
            "switch" to TableFormObj(
                type = FormFieldType.SwitchButton(
                    onEnable = { data ->
                        val responseRateDataKot = data as ResponseRateDataKot
                        startTask(responseRateDataKot,toastUtils)
                    },
                    onDisable = { data ->
                        val responseRateDataKot = data as ResponseRateDataKot
                        stopTask(responseRateDataKot,toastUtils)
                    }
                )
            )
        )

        TableUtils.EditableTable(
            tableHead = tableHead,
            tableForm = tableForm,
            loadData = { page, pageSize ->
                loadResponseRateData(
                    page,
                    pageSize
                )
            },
            onSave = { data ->
                val result = saveResponseRateData(data)
                if(result){
                    toastUtils.success("保存成功")
                    refreshData?.invoke()
                }else{
                    toastUtils.success("保存失败")
                }

            },
            onDelete = { data ->
                val result = deleteResponseRateData(data)
                if(result){
                    toastUtils.success("删除成功")
                    refreshData?.invoke()
                }else{
                    toastUtils.success("删除失败")
                }
            },
            onAdd = {
                showAddDeviceDialog = true
            },
            // 将数据重载函数响应回来，可支持手动调用刷新信息
            onRefresh = { callback ->
                refreshData = callback // 将刷新回调函数保存到外部变量
            }
        )

        if (showAddDeviceDialog) {
            val windowState = rememberWindowState(
                width = 450.dp,
                height = Dp.Unspecified
            )
            Window(
                onCloseRequest = { showAddDeviceDialog = false },
                title = "新增拼多多自动回复",
                state = windowState
            ) {
                AppTheme {
                    DraggableAddDeviceDialogContent(
                        onClose = { showAddDeviceDialog = false },
                        refreshData = {
                            refreshData?.invoke() // 保存成功后刷新表格数据
                        }
                    )
                }
            }
        }
    }
}
