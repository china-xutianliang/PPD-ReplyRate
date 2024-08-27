package utils

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.*
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import java.io.IOException
import java.util.*

object JsonUtils {

    private val LOGGER = LoggerFactory.getLogger(JsonUtils::class.java)

    private val OBJECT_MAPPER = ObjectMapper()
    private val WRAP_OBJECT_MAPPER = ObjectMapper()

    init {
        // 转字符串时, 忽略 NULL
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        // 转对象时, 忽略多余字段
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)

        WRAP_OBJECT_MAPPER.enable(DeserializationFeature.UNWRAP_ROOT_VALUE)
        WRAP_OBJECT_MAPPER.enable(SerializationFeature.WRAP_ROOT_VALUE)
        WRAP_OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        WRAP_OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

    fun <T> jsonToObj(jsonString: String?, clazz: Class<T>): T? {
        if (StringUtils.isBlank(jsonString)) {
            return null
        }
        return try {
            OBJECT_MAPPER.readValue(jsonString, clazz)
        } catch (e: Exception) {
            LOGGER.error("json2Pojo error jsonString: {}", jsonString, e)
            null
        }
    }

    fun objToJson(obj: Any?): String? {
        if (obj == null) {
            return null
        }
        return try {
            OBJECT_MAPPER.writeValueAsString(obj)
        } catch (e: Exception) {
            LOGGER.error("objToJson error", e)
            null
        }
    }

    /**
     * json转对象(可选解析根节点，对象内注解)
     *
     * @param jsonStr 待转换的json字符串
     * @param clz 待转换的对象类
     * @param rootname 是否解析根节点
     * @return Object
     */
    fun jsonToObj(jsonStr: String?, clz: Class<*>, rootname: Boolean): Any? {
        if (StringUtils.isBlank(jsonStr)) {
            return null
        }
        return try {
            if (rootname) {
                WRAP_OBJECT_MAPPER.readValue(jsonStr, clz)
            } else {
                OBJECT_MAPPER.readValue(jsonStr, clz)
            }
        } catch (e: Exception) {
            LOGGER.error("jsonToObj error", e)
            null
        }
    }

    /**
     * 对象转json字符串(可选是否添加根节点，对象内注解)
     *
     * @param obj 待转换的对象
     * @param rootname 是否添加根节点
     * @return json字符串
     */
    fun objToJson(obj: Any?, rootname: Boolean): String? {
        if (obj == null) {
            return null
        }
        return try {
            if (rootname) {
                WRAP_OBJECT_MAPPER.writeValueAsString(obj)
            } else {
                OBJECT_MAPPER.writeValueAsString(obj)
            }
        } catch (e: Exception) {
            LOGGER.error("objToJson error", e)
            null
        }
    }

    fun <T> json2Pojo(jsonString: String?, clazz: Class<T>): T? {
        if (StringUtils.isBlank(jsonString)) {
            return null
        }
        return try {
            OBJECT_MAPPER.readValue(jsonString, clazz)
        } catch (e: Exception) {
            LOGGER.error("json2Pojo error jsonString:$jsonString", e)
            null
        }
    }

    fun <T> json2Pojo(jsonString: String?, typeReference: TypeReference<T>): T? {
        if (StringUtils.isBlank(jsonString)) {
            return null
        }
        return try {
            OBJECT_MAPPER.readValue(jsonString, typeReference)
        } catch (e: Exception) {
            LOGGER.error("json2Pojo error jsonString:$jsonString", e)
            null
        }
    }

    fun <T> json2PojoV2(jsonString: String, typeReference: TypeReference<T>): T? {
        return try {
            OBJECT_MAPPER.readValue(jsonString, typeReference)
        } catch (e: Exception) {
            LOGGER.error("json2PojoV2 error {}", jsonString)
            null
        }
    }

    /** 将json字符串转成相应的List */
    fun <T> convertJson2List(jsonStr: String, clazz: Class<T>): List<T>? {
        val javaType: JavaType = OBJECT_MAPPER.typeFactory.constructCollectionType(ArrayList::class.java, clazz)
        return try {
            OBJECT_MAPPER.readValue(jsonStr, javaType)
        } catch (e: IOException) {
            LOGGER.error("convertJson2List error jsonStr:$jsonStr", e)
            null
        }
    }

    fun json2Map(jsonString: String?): Map<String, String>? {
        if (StringUtils.isBlank(jsonString)) {
            return null
        }
        return try {
            OBJECT_MAPPER.readValue(jsonString, object : TypeReference<Map<String, String>>() {})
        } catch (e: Exception) {
            LOGGER.error("json2Map error jsonString:$jsonString", e)
            null
        }
    }

    @Throws(Exception::class)
    fun json2MapV2(jsonString: String?): Map<String, String>? {
        if (StringUtils.isBlank(jsonString)) {
            throw IllegalArgumentException("数据为空")
        }
        return try {
            OBJECT_MAPPER.readValue(jsonString, object : TypeReference<Map<String, String>>() {})
        } catch (e: JsonProcessingException) {
            LOGGER.error("json2MapV2 error : {}", e.message)
            throw e
        }
    }

    @Throws(Exception::class)
    fun json2MapV3(jsonString: String): Map<String, String>? {
        return try {
            OBJECT_MAPPER.readValue(jsonString, object : TypeReference<Map<String, String>>() {})
        } catch (e: JsonProcessingException) {
            LOGGER.error("json2MapV3 error : {}", e.message)
            throw e
        }
    }

    fun strToJsonNode(str: String): JsonNode? {
        Objects.requireNonNull(str)
        return try {
            OBJECT_MAPPER.readTree(str)
        } catch (ex: IOException) {
            LOGGER.error("str=$str failure to JsonNode")
            null
        }
    }
}
