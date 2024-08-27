import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.*
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.*
import kotlinx.coroutines.*
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

/**
 * 自定义表格组件声明
 */
sealed class FormFieldType {
    object Text : FormFieldType()
    object EditableText : FormFieldType()
    data class SwitchButton(
        val onEnable: (suspend (Any) -> Boolean)? = null,
        val onDisable: (suspend (Any) -> Boolean)? = null
    ) : FormFieldType()
    data class Dropdown(val options: List<Pair<String, String>>) : FormFieldType()
}

/**
 * 表头对象
 */
data class TableHeadObj(
    val description: String,
    var width: Dp = 100.dp,
    var alignment: Alignment = Alignment.Center
)

/**
 * 表格对象
 */
data class TableFormObj(
    var fieldName: String? = null,
    var width: Dp = 100.dp,
    var alignment: Alignment = Alignment.Center,
    var fieldValue: String? = null,
    var type: FormFieldType = FormFieldType.Text
)

/**
 * 分页数据
 */
data class FormPageDataResult<T>(
    val count: Int,
    val data: List<T>
)

object ObjectUtils {
    fun <T : Any> toMap(obj: T): Map<String, Any?> {
        return obj::class.memberProperties.associate { prop ->
            prop.name to prop.call(obj)
        }
    }

    fun <T : Any> updateField(obj: T, fieldName: String, newValue: Any): T {
        val prop = obj::class.memberProperties.find { it.name == fieldName } as? KMutableProperty1<T, Any?>
        prop?.isAccessible = true
        val primaryConstructor = obj::class.primaryConstructor
        val params = primaryConstructor?.parameters?.associateWith { param ->
            if (param.name == fieldName) {
                when (param.type.classifier) {
                    String::class -> newValue.toString()
                    Int::class -> if (newValue is Boolean) if (newValue) 1 else 0 else newValue.toString().toIntOrNull()
                        ?: newValue as? Int

                    Boolean::class -> newValue.toString().toBoolean()
                    else -> newValue
                }
            } else {
                obj::class.memberProperties.find { it.name == param.name }?.getter?.call(obj)
            }
        }
        return primaryConstructor?.callBy(params.orEmpty()) ?: obj
    }
}


object TableUtils {
    @Composable
    fun <T : Any> EditableTable(
        tableHead: Map<String, TableHeadObj>,
        tableForm: Map<String, TableFormObj>,
        loadData: (page: Long, pageSize: Long) -> FormPageDataResult<T>,
        onSave: (data: List<Any>) -> Unit,
        onDelete: (data: List<Any>) -> Unit,
        onAdd: () -> Unit,
        initialPageSize: Long = 10,
        tableWidth: Dp = 1200.dp,
        tableHeight: Dp = 600.dp,
        onRefresh: (refresh: () -> Unit) -> Unit
    ) {
        // 动态数据绑定渲染
        var currentPage by remember { mutableStateOf(1L) }
        var pageSize by remember { mutableStateOf(initialPageSize) }
        var dataResult by remember { mutableStateOf(loadData(currentPage, pageSize)) }
        val paginatedData = remember { mutableStateListOf<T>().apply { addAll(dataResult.data) } }
        val selectedItems = remember { mutableStateListOf<Int>() }
        var allSelected by remember { mutableStateOf(false) }

        // 用于手动触发数据刷新
        val refreshDataCallback = {
            dataResult = loadData(currentPage, pageSize)
            paginatedData.clear()
            paginatedData.addAll(dataResult.data)
            selectedItems.clear()
            if (paginatedData.isEmpty() && currentPage > 1) {
                currentPage--
                dataResult = loadData(currentPage, pageSize)
                paginatedData.clear()
                paginatedData.addAll(dataResult.data)
                selectedItems.clear()
            }
        }

        // 将刷新回调函数传递给外部
        onRefresh(refreshDataCallback)

        LaunchedEffect(currentPage, pageSize) {
            refreshDataCallback()
        }

        val totalPages = (dataResult.count + pageSize - 1) / pageSize

        Column(modifier = Modifier.width(tableWidth).padding(8.dp)) {
            // 新增删除保存按钮
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .width(tableWidth),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Button(
                        onClick = onAdd,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("新增", fontSize = 14.sp, color = MaterialTheme.colorScheme.surface)
                    }
                    Button(
                        onClick = {
                            onSave(paginatedData)
                            refreshDataCallback()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("保存", fontSize = 14.sp, color = MaterialTheme.colorScheme.onPrimary)
                    }
                    Button(
                        onClick = {
                            onDelete(paginatedData.filterIndexed { index, _ -> index in selectedItems })
                            refreshDataCallback()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("删除", fontSize = 14.sp, color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }

            // 表格
            val verticalScrollState = rememberScrollState()
            val horizontalScrollState = rememberScrollState()
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(tableHeight)
                    .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(verticalScrollState)
                        .horizontalScroll(horizontalScrollState)
                ) {
                    createTable(
                        tableHead,
                        tableForm,
                        paginatedData,
                        selectedItems,
                        onDataChange = { index, fieldName, newValue ->
                            val updatedData = ObjectUtils.updateField(paginatedData[index], fieldName, newValue)
                            paginatedData[index] = updatedData
                        },
                        onSelectAllChange = { isSelected ->
                            allSelected = isSelected
                        },
                        currentPage = currentPage,
                        pageSize = pageSize,
                        allSelected = allSelected
                    )
                }

                VerticalScrollbar(
                    adapter = rememberScrollbarAdapter(verticalScrollState),
                    modifier = Modifier.align(Alignment.CenterEnd)
                )

                HorizontalScrollbar(
                    adapter = rememberScrollbarAdapter(horizontalScrollState),
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }

            // 分页
            Row(
                modifier = Modifier.padding(top = 16.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PaginationControls(
                    currentPage = currentPage,
                    totalPages = totalPages,
                    onPrevious = {
                        if (currentPage > 1) {
                            currentPage--
                            refreshDataCallback()
                        }
                        allSelected = false // 取消表头选择
                    },
                    onNext = {
                        if (currentPage < totalPages) {
                            currentPage++
                            refreshDataCallback()
                        }
                        allSelected = false // 取消表头选择
                    }
                )

                PageSizeSelector(
                    pageSize = pageSize,
                    onPageSizeChange = { newSize ->
                        pageSize = newSize
                        currentPage = 1
                        refreshDataCallback()
                        allSelected = false // 取消表头选择
                    }
                )
            }
        }
    }


    @Composable
    private fun <T : Any> createTable(
        tableHead: Map<String, TableHeadObj>,
        tableForm: Map<String, TableFormObj>,
        datas: SnapshotStateList<T>,
        selectedItems: SnapshotStateList<Int>,
        onDataChange: (Int, String, Any) -> Unit,
        onSelectAllChange: (Boolean) -> Unit,
        currentPage: Long,
        pageSize: Long,
        allSelected: Boolean
    ) {
        Column {
            val newTableForm = tableHead.mapValues { (key, obj) ->
                TableFormObj(
                    fieldName = key,
                    width = obj.width,
                    alignment = tableForm[key]?.alignment ?: obj.alignment,
                    type = tableForm[key]?.type ?: FormFieldType.Text
                )
            }

            // 表头渲染
            Row(modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant)) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .width(50.dp)
                        .height(46.dp)
                        .border(width = 0.5.dp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
                        .padding(vertical = 8.dp)
                ) {
                    Checkbox(
                        checked = allSelected,
                        onCheckedChange = { checked ->
                            onSelectAllChange(checked)
                            selectedItems.clear()
                            if (checked) {
                                selectedItems.addAll(datas.indices)
                            }
                        },
                        colors = CheckboxDefaults.colors(MaterialTheme.colorScheme.primary)
                    )
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .width(50.dp)
                        .height(46.dp)
                        .border(width = 0.5.dp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
                ) {
                    Text(
                        "序号",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.W500,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                tableHead.forEach { (key, obj) ->
                    Box(
                        contentAlignment = obj.alignment,
                        modifier = Modifier
                            .width(obj.width)
                            .height(46.dp)
                            .border(width = 0.5.dp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
                    ) {
                        Text(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.W500,
                            text = obj.description,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // 表格内容渲染
            datas.forEachIndexed { index, data ->
                TableRow(
                    newTableForm = newTableForm,
                    data = data,
                    rowIndex = index,
                    selectedItems = selectedItems,
                    onDataChange = { fieldName, newValue ->
                        onDataChange(index, fieldName, newValue)
                    },
                    currentPage = currentPage,
                    pageSize = pageSize
                )
            }
        }
    }

    @Composable
    private fun <T : Any> TableRow(
        newTableForm: Map<String, TableFormObj>,
        data: T,
        rowIndex: Int,
        selectedItems: SnapshotStateList<Int>,
        onDataChange: (String, Any) -> Unit,
        currentPage: Long,
        pageSize: Long
    ) {
        val beanToMap = ObjectUtils.toMap(data)
        val isSelected = rowIndex in selectedItems
        val globalIndex = (currentPage - 1) * pageSize + rowIndex + 1

        Row(
            modifier = Modifier.border(
                width = 0.5.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
            )
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .border(width = 0.5.dp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
            ) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = { checked ->
                        if (checked) {
                            selectedItems.add(rowIndex)
                        } else {
                            selectedItems.remove(rowIndex)
                        }
                    },
                    colors = CheckboxDefaults.colors(MaterialTheme.colorScheme.primary)
                )
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .border(width = 0.5.dp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
            ) {
                Text(text = globalIndex.toString(), color = MaterialTheme.colorScheme.onSurface)
            }

            newTableForm.forEach { (fieldName, formObj) ->
                val fieldValue = beanToMap[fieldName]?.toString() ?: ""
                Box(
                    contentAlignment = formObj.alignment,
                    modifier = Modifier
                        .width(formObj.width)
                        .height(50.dp)
                        .border(width = 0.5.dp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
                ) {
                    when (formObj.type) {
                        is FormFieldType.Text -> Text(text = fieldValue, color = MaterialTheme.colorScheme.onSurface)
                        is FormFieldType.EditableText -> {
                            var textState by rememberSaveable { mutableStateOf(TextFieldValue(fieldValue)) }
                            val coroutineScope = rememberCoroutineScope()
                            var job by remember { mutableStateOf<Job?>(null) }

                            LaunchedEffect(fieldValue) {
                                if (textState.text != fieldValue) {
                                    textState = TextFieldValue(fieldValue)
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .clickable { }
                                    .fillMaxSize()
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(horizontal = 10.dp)
                                            .clipToBounds(),
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        BasicTextField(
                                            value = textState,
                                            onValueChange = { newValue ->
                                                textState = newValue
                                                job?.cancel()
                                                job = coroutineScope.launch {
                                                    delay(300) // 延迟300毫秒
                                                    onDataChange(fieldName, newValue.text)
                                                }
                                            },
                                            textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface),
                                            singleLine = true,
                                            decorationBox = { innerTextField ->
                                                Box(
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    if (textState.text.isEmpty()) {
                                                        Text("请输入...", color = MaterialTheme.colorScheme.onSurface)
                                                    }
                                                    innerTextField()
                                                }
                                            }
                                        )
                                    }
                                    Icon(
                                        modifier = Modifier.padding(end = 10.dp),
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = null
                                    )
                                }
                            }
                        }

                        is FormFieldType.Dropdown -> {
                            var selectedOption by remember { mutableStateOf(fieldValue) }
                            var expanded by remember { mutableStateOf(false) }
                            LaunchedEffect(fieldValue) {
                                if (selectedOption != fieldValue) {
                                    selectedOption = fieldValue
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .clickable { expanded = true }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = (formObj.type as? FormFieldType.Dropdown)?.options?.find { it.first == selectedOption }?.second
                                            ?: "请选择...",
                                        color = if (selectedOption.isEmpty()) MaterialTheme.colorScheme.onSurface.copy(
                                            alpha = 0.6f
                                        ) else MaterialTheme.colorScheme.onSurface
                                    )
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = null,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    (formObj.type as? FormFieldType.Dropdown)?.options?.forEach { option ->
                                        DropdownMenuItem(
                                            text = { Text(option.second) },
                                            onClick = {
                                                selectedOption = option.first
                                                onDataChange(fieldName, option.first)
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        is FormFieldType.SwitchButton -> {
                            var selectedState by remember { mutableStateOf(fieldValue.toBoolean()) }
                            val coroutineScope = rememberCoroutineScope()

                            LaunchedEffect(fieldValue) {
                                selectedState = fieldValue.toBoolean()
                            }

                            Switch(
                                checked = selectedState,
                                onCheckedChange = { isChecked ->
                                    coroutineScope.launch {
                                        val formSwitchButton = formObj.type as? FormFieldType.SwitchButton
                                        // 开或者关的回调函数选择
                                        val onClick = if (isChecked) formSwitchButton?.onEnable else formSwitchButton?.onDisable

                                        if (onClick != null) {
                                            val success = withContext(Dispatchers.IO) {
                                                onClick.invoke(data)
                                            }
                                            if (success) {
                                                selectedState = isChecked
                                                onDataChange(fieldName, isChecked)
                                            } else {
                                                // 如果失败，恢复原始状态
                                                selectedState = !isChecked
                                                onDataChange(fieldName, !isChecked)
                                            }
                                        } else {
                                            selectedState = isChecked
                                            onDataChange(fieldName, isChecked)
                                        }
                                    }
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                                    uncheckedThumbColor = MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    @Composable
    fun PaginationControls(currentPage: Long, totalPages: Long, onPrevious: () -> Unit, onNext: () -> Unit) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onPrevious,
                enabled = currentPage > 1,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text("上一页", fontSize = 14.sp, color = MaterialTheme.colorScheme.onPrimary)
            }
            Text(
                "第 $currentPage 页，共 $totalPages 页",
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Button(
                onClick = onNext,
                enabled = currentPage < totalPages,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text("下一页", fontSize = 14.sp, color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }

    @Composable
    fun PageSizeSelector(pageSize: Long, onPageSizeChange: (Long) -> Unit) {
        var expanded by remember { mutableStateOf(false) }
        val pageSizes = listOf(10L, 20L, 30L, 40L, 50L)

        Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)) {
            Row(
                modifier = Modifier.clickable { expanded = true },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("每页显示: $pageSize", fontSize = 14.sp, modifier = Modifier.padding(end = 8.dp))
                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                pageSizes.forEach { size ->
                    DropdownMenuItem(
                        text = { Text(size.toString(), fontSize = 14.sp) },
                        onClick = {
                            onPageSizeChange(size)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
