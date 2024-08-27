package com.xtl.ebusiness.system.setting.questionAnswer.page

import TableFormObj
import TableHeadObj
import ToastUtils
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import com.xtl.ebusiness.system.setting.questionAnswer.funtion.deleteQuestionAnswerData
import com.xtl.ebusiness.system.setting.questionAnswer.funtion.loadQuestionAnswerData
import com.xtl.ebusiness.system.setting.questionAnswer.funtion.saveQuestionAnswerData
import rememberToastUtils
import ui.theme.AppTheme

@Preview
@Composable
fun QuestionAnswerScreen() {
    val toastUtils = rememberToastUtils()
    // 控制弹窗是否展示
    var showAddQuestionAnswerDialog by remember { mutableStateOf(false) }
    // 数据重新加载回调函数
    var refreshData by remember { mutableStateOf<(() -> Unit)?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Spacer(modifier = Modifier.height(16.dp))

        val tableHead = mapOf(
            "question" to TableHeadObj(description = "问题", width = 250.dp),
            "answer" to TableHeadObj(description = "答案", width = 250.dp),
        )

        val tableForm = mapOf(
            "answer" to TableFormObj(type = FormFieldType.EditableText)
        )

        TableUtils.EditableTable(
            tableHead = tableHead,
            tableForm = tableForm,
            loadData = { page, pageSize ->
                loadQuestionAnswerData(
                    page,
                    pageSize
                )
            },
            onSave = { data ->
                val result = saveQuestionAnswerData(data)
                if(result){
                    toastUtils.success("保存成功")
                    refreshData?.invoke()
                }else{
                    toastUtils.success("保存失败")
                }
            },
            onDelete = { data ->
                val result = deleteQuestionAnswerData(data)
                if(result){
                    toastUtils.success("删除成功")
                    refreshData?.invoke()
                }else{
                    toastUtils.error("删除失败")
                }
            },
            onAdd = {
                showAddQuestionAnswerDialog = true
            },
            // 将数据重载函数响应回来，可支持手动调用刷新信息
            onRefresh = { callback ->
                refreshData = callback // 将刷新回调函数保存到外部变量
            }
        )

        if (showAddQuestionAnswerDialog) {
            val windowState = rememberWindowState(
                width = 450.dp,
                height = Dp.Unspecified
            )
            Window(
                onCloseRequest = { showAddQuestionAnswerDialog = false },
                title = "问题库",
                state = windowState
            ) {
                AppTheme {
                    AddQuestionAnswerDialog(
                        onClose = { showAddQuestionAnswerDialog = false },
                        refreshData = {
                            refreshData?.invoke() // 保存成功后刷新表格数据
                        }
                    )
                }
            }
        }
    }
}
