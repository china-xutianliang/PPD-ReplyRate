package com.xtl.ebusiness.system.setting.questionAnswer.funtion

import FormPageDataResult
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.xtl.ebusiness.entity.QuestionAnswer
import com.xtl.ebusiness.service.impl.QuestionAnswerServiceImpl
import com.xtl.ebusiness.system.setting.questionAnswer.date.QuestionAnswerKot
import com.xtl.ecore.utils.SpringUtils

/**
 * 表格数据加载
 */
fun loadQuestionAnswerData(page: Long, pageSize: Long): FormPageDataResult<QuestionAnswerKot> {
    val questionAnswerService = SpringUtils.getBean(QuestionAnswerServiceImpl::class.java)
    val pageRequest = Page<QuestionAnswer>(page, pageSize)
    val pageResult: Page<QuestionAnswer> = questionAnswerService.page(pageRequest)

    val data = pageResult.records.map { record ->
        QuestionAnswerKot(
            question = record.question,
            answer = record.answer
        )
    }

    return FormPageDataResult(count = pageResult.total.toInt(), data = data)
}

/**
 * 新增数据
 */
fun addQuestionAnswerData(data: QuestionAnswer): Boolean {

    var questionAnswerService = SpringUtils.getBean(QuestionAnswerServiceImpl::class.java)

    return try {
        questionAnswerService.save(data)
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

/**
 * 表格数据保存
 */
fun saveQuestionAnswerData(data: List<Any>) : Boolean {
    val questionAnswerService = SpringUtils.getBean(QuestionAnswerServiceImpl::class.java)
    // Any类型数据转换为 QuestionAnswerKot
    val QuestionAnswerDataList = data.filterIsInstance<QuestionAnswerKot>().map { questionAnswerKot ->
        QuestionAnswer().apply {
            this.question = questionAnswerKot.question
            this.answer = questionAnswerKot.answer
        }
    }
    return questionAnswerService.saveOrUpdateBatch(QuestionAnswerDataList)
}

/**
 * 表格数据删除
 */
fun deleteQuestionAnswerData(data: List<Any>) : Boolean {
    val questionAnswerService = SpringUtils.getBean(QuestionAnswerServiceImpl::class.java)
    val idsToDelete = data.filterIsInstance<QuestionAnswerKot>().map { it.question }
    return questionAnswerService.removeByIds(idsToDelete)
}
