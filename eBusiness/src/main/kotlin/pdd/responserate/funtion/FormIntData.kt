package com.xtl.ebusiness.pdd.responserate.funtion

import FormPageDataResult
import ToastUtils
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.xtl.ebusiness.entity.ResponseRateData
import com.xtl.ebusiness.service.ResponseRateDataService
import com.xtl.ebusiness.service.replyRateBooster.impl.PddReplyRateBooster
import com.xtl.ebusiness.service.simulator.Simulator
import com.xtl.ebusiness.utils.TaskUtils
import com.xtl.ecore.enums.ResponseCode
import com.xtl.ecore.utils.SpringUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pdd.responserate.data.ResponseRateDataKot


/**
 * 表格数据加载
 */
fun loadResponseRateData(page: Long, pageSize: Long): FormPageDataResult<ResponseRateDataKot> {
    val responseRateDataService = SpringUtils.getBean(ResponseRateDataService::class.java)
    val pageRequest = Page<ResponseRateData>(page, pageSize)
    val pageResult: Page<ResponseRateData> = responseRateDataService.page(pageRequest)

    val data = pageResult.records.map { responseRateData ->
        ResponseRateDataKot(
            id = responseRateData.id.toString(),
            simulatorType = responseRateData.simulatorType,
            simulatorName = responseRateData.simulatorName,
            roleType = responseRateData.roleType,
            chatName = responseRateData.chatName
        )
    }

    return FormPageDataResult(count = pageResult.total.toInt(), data = data)
}

/**
 * 新增数据
 */
fun addResponseRateData(data: ResponseRateData): Boolean {

    var responseRateDataService = SpringUtils.getBean(ResponseRateDataService::class.java)

    return try {
        responseRateDataService.save(data)
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

/**
 * 表格数据保存
 */
fun saveResponseRateData(data: List<Any>) : Boolean {
    val responseRateDataService = SpringUtils.getBean(ResponseRateDataService::class.java)

    // Any类型数据转换为 ResponseRateDataKot
    val responseRateDataList = data.filterIsInstance<ResponseRateDataKot>().map { responseRateDataKot ->
        ResponseRateData().apply {
            this.id = responseRateDataKot.id
            this.simulatorType = responseRateDataKot.simulatorType
            this.simulatorName = responseRateDataKot.simulatorName
            this.roleType = responseRateDataKot.roleType
            this.chatName = responseRateDataKot.chatName
        }
    }
    return responseRateDataService.saveOrUpdateBatch(responseRateDataList)
}

/**
 * 表格数据保存
 */
fun deleteResponseRateData(data: List<Any>) : Boolean {
    val responseRateDataService = SpringUtils.getBean(ResponseRateDataService::class.java)
    val idsToDelete = data.filterIsInstance<ResponseRateDataKot>().map { it.id }
    return responseRateDataService.removeByIds(idsToDelete)
}

suspend fun startTask(data: ResponseRateDataKot,toastUtils: ToastUtils): Boolean {

    val simulator = SpringUtils.getBean(Simulator::class.java)
    val pddReplyRateBooster = SpringUtils.getBean(PddReplyRateBooster::class.java)

    return try {

        withContext(Dispatchers.Main) {
            LoadingUtils.show("启动中...")
        }

        var deviceId = simulator.startSimulatorByDeviceName(data.simulatorType, data.simulatorName)

        val resp = pddReplyRateBooster.startReplyRateTask(data.roleType, deviceId, data.chatName)

        if (resp.code == ResponseCode.OK.getCode()) {
            data.taskId = resp.result as? String
            toastUtils.success("启动成功")
            true
        } else {
            toastUtils.error("启动失败: ${resp.msg}")
            false
        }
    } catch (e: Exception) {
        toastUtils.error(e.message ?: "未知错误")
        false
    } finally {
        // 关闭加载对话框
        withContext(Dispatchers.Main) {
            LoadingUtils.dismiss()
        }
    }
}

suspend fun stopTask(data: ResponseRateDataKot,toastUtils: ToastUtils): Boolean {
    return try {
        withContext(Dispatchers.Main) {
            LoadingUtils.show("关闭中...")
        }
        val resp = TaskUtils.cancelTask(data.taskId)
        if (resp.code == ResponseCode.OK.getCode()) {
            data.taskId = null
            toastUtils.success("关闭成功")
            true
        } else {
            toastUtils.error("关闭失败: ${resp.msg}")
            false
        }
    } catch (e: Exception) {
        toastUtils.error(e.message ?: "未知错误")
        false
    } finally {
        // 关闭加载对话框
        withContext(Dispatchers.Main) {
            LoadingUtils.dismiss()
        }
    }
}
