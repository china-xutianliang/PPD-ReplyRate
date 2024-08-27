package utils

import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

object BeanUtils {

    /**
     * 将源对象的属性复制到目标对象中，只有属性名和类型都匹配的属性才会被复制。
     *
     * @param source 源对象
     * @param target 目标对象
     */
    fun <S : Any, T : Any> copyProperties(source: S, target: T) {
        val sourceProperties = source::class.memberProperties
        val targetProperties = target::class.memberProperties.filterIsInstance<KMutableProperty1<T, Any?>>()

        for (sourceProp in sourceProperties) {
            val matchingTargetProp = targetProperties.find { targetProp ->
                // targetProp.returnType.classifier 只关注类型的主要分类器（即忽略了平台类型的细节）
                targetProp.name == sourceProp.name && targetProp.returnType.classifier == sourceProp.returnType.classifier
            }
            if (matchingTargetProp != null) {
                try {
                    @Suppress("UNCHECKED_CAST")
                    val value = (sourceProp as KProperty1<S, Any?>).get(source)
                    matchingTargetProp.set(target, value)
                } catch (e: Exception) {
                    println("Failed to set ${matchingTargetProp.name}: ${e.message}")
                }
            }
        }
    }

    /**
     * 将源对象的属性复制到目标类的实例中，并返回该实例。
     *
     * @param source 源对象
     * @return 目标类的实例，属性已从源对象中复制
     */
    inline fun <S : Any, reified T : Any> copyPropertiesToNewObject(source: S): T {
        val targetInstance = T::class.java.getDeclaredConstructor().newInstance()
        copyProperties(source, targetInstance)
        return targetInstance
    }
}
