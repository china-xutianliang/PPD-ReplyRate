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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.xtl.ebusiness.entity.ResponseRateData
import com.xtl.ebusiness.pdd.responserate.funtion.addResponseRateData
import pdd.responserate.data.ResponseRateTypeOptions
import pdd.responserate.data.SimulatorTypeOptions
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DraggableAddDeviceDialogContent(
    onClose: () -> Unit,
    refreshData: () -> Unit
){
    var simulatorType by remember { mutableStateOf("") }
    var simulatorName by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var chatName by remember { mutableStateOf("") }
    var expandedType by remember { mutableStateOf(false) }
    var expandedSimulatorType by remember { mutableStateOf(false) }

    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    var showAddDate by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

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


        // 模拟器类型下拉菜单
        ExposedDropdownMenuBox(
            expanded = expandedSimulatorType,
            onExpandedChange = { expandedSimulatorType = !expandedSimulatorType }
        ) {
            OutlinedTextField(
                readOnly = true,
                value = SimulatorTypeOptions.find { it.first == simulatorType }?.second ?: "请选择模拟器类型",
                onValueChange = {},
                label = { Text("模拟器类型") },
                trailingIcon = {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expandedSimulatorType,
                onDismissRequest = { expandedSimulatorType = false }
            ) {
                SimulatorTypeOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.second) },
                        onClick = {
                            simulatorType = option.first
                            expandedSimulatorType = false
                        }
                    )
                }
            }
        }

        // 模拟器启动名称输入框
        OutlinedTextField(
            value = simulatorName,
            onValueChange = { simulatorName = it },
            label = { Text("模拟器启动名称") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // 角色类型下拉菜单
        ExposedDropdownMenuBox(
            expanded = expandedType,
            onExpandedChange = { expandedType = !expandedType }
        ) {
            OutlinedTextField(
                readOnly = true,
                value = ResponseRateTypeOptions.find { it.first == type }?.second ?: "请选择角色类型",
                onValueChange = {},
                label = { Text("类型") },
                trailingIcon = {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expandedType,
                onDismissRequest = { expandedType = false }
            ) {
                ResponseRateTypeOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.second) },
                        onClick = {
                            type = option.first
                            expandedType = false
                        }
                    )
                }
            }
        }


        // 聊天对象名称输入框
        OutlinedTextField(
            value = chatName,
            onValueChange = { chatName = it },
            label = { Text("聊天对象名称") },
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
                val responseRateData = ResponseRateData().apply {
                    this.simulatorType = simulatorType.toInt()
                    this.simulatorName = simulatorName
                    this.roleType = type.toInt()
                    this.chatName = chatName
                }
                val success = addResponseRateData(responseRateData)
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
                    TextButton(onClick = { showAddDate = false; if (dialogMessage == "添加成功") onClose() }) {
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
