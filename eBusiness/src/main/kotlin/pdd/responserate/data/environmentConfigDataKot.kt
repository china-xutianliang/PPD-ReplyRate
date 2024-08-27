package pdd.responserate.data


/**
 * 设备数据类
 */
data class ResponseRateDataKot(
    val id: String,
    val simulatorType : Int, // 模拟器类型  0雷电 1闪现 2机电 等等
    val simulatorName : String, // 模拟器启动名称
    val roleType: Int,  // 类型：商家、用户 0商家 1顾客
    val chatName: String, // 聊天对象名称
    val switch: Boolean = false, // 启动 关闭


    var taskId : String? = null // 启动返回的任务ID

)

// 类型枚举
val ResponseRateTypeOptions = listOf(
    "0" to "商家",
    "1" to "客户"
)

val SimulatorTypeOptions = listOf(
    "0" to "雷电"
)
