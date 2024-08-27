package com.xtl.ebusiness.system.setting.questionAnswer.page

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.IntOffset
import com.xtl.ebusiness.entity.QuestionAnswer
import com.xtl.ebusiness.system.setting.questionAnswer.funtion.addQuestionAnswerData
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddQuestionAnswerDialog(
    onClose: () -> Unit,
    refreshData: () -> Unit
) {
    var question by remember { mutableStateOf("") }
    var answer by remember { mutableStateOf("") }

    var showAddDate by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    Column(
        modifier = Modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            }
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = question,
            onValueChange = { question = it },
            label = { Text("问题") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        OutlinedTextField(
            value = answer,
            onValueChange = { answer = it },
            label = { Text("答案") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 按钮行
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onClose) {
                Text("取消")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                val questionAnswer = QuestionAnswer().apply {
                    this.question = question
                    this.answer = answer
                }
                val success = addQuestionAnswerData(questionAnswer)
                dialogMessage = if (success) {
                    "添加成功"
                } else {
                    "添加失败"
                }
                showAddDate = true
            }) {
                Text("添加")
            }
        }

        if (showAddDate) {
            AlertDialog(
                onDismissRequest = { showAddDate = false },
                confirmButton = {
                    TextButton(onClick = {
                        showAddDate = false
                        if (dialogMessage == "添加成功") onClose()
                    }) {
                        Text("确定")
                    }
                },
                title = { Text("结果") },
                text = { Text(dialogMessage) }
            )
            refreshData()
        }
    }
}
