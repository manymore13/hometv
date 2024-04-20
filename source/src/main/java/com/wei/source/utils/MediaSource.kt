package com.wei.source.utils

import com.tencent.mmkv.MMKV
import com.wei.source.utils.MediaSource.Companion.DEFAULT_SOURCE_NAME
import com.wei.source.utils.MediaSource.Companion.SOURCE_NAME_LIST
import java.math.BigInteger
import java.security.MessageDigest

val kv: MMKV = MMKV.defaultMMKV()

data class MediaSource(val sourceName: String) {

    companion object {

        const val DEFAULT_SOURCE_NAME = "default_source_name"
        const val SOURCE_NAME_LIST = "source_name_list"

        fun getDefaultSourceName(): String {
            return kv.decodeString(DEFAULT_SOURCE_NAME) ?: ""
        }

        fun getSourceNameList(): MutableSet<String>? {
            return kv.decodeStringSet(SOURCE_NAME_LIST)
        }
    }

    constructor(sourceName: String, sourceUrl: String) : this(sourceName) {
        this.sourceUrl = sourceUrl
    }

    private val key: String by lazy {
        sourceName.md5()
    }

    val recordTimeKey = key + "_time"
    val sourceStrKey = key + "_source"
    val sourceUrlKey = key + "_url"

    val recordTime: Long
        get() = kv.decodeLong(recordTimeKey, 0L)

    val sourceContent: String?
        get() = kv.decodeString(sourceStrKey, null)

    var sourceUrl: String = ""
        get() {
            if (field.isEmpty()) {
                return kv.decodeString(sourceUrlKey) ?: ""
            } else {
                return field
            }
        }
        set(value) {
            field = value
        }

}

fun MediaSource.save(time: Long, sourceStr: String) {
    kv.encode(recordTimeKey, time)
    kv.encode(sourceStrKey, sourceStr)
    kv.encode(sourceUrlKey, sourceUrl)
    setDefault()
    saveToSourceNameList()
}

/**
 * 设置默认的源名称
 */
fun MediaSource.setDefault() {
    kv.encode(DEFAULT_SOURCE_NAME, sourceName)
}

/**
 * 保存到源目录列表中
 */
private fun MediaSource.saveToSourceNameList() {
    var sourceNameSet: MutableSet<String>? = kv.decodeStringSet(SOURCE_NAME_LIST)?.toMutableSet()
    if (sourceNameSet == null) {
        sourceNameSet = mutableSetOf(sourceName)
    } else {
        sourceNameSet.add(sourceName)
    }
    kv.encode(SOURCE_NAME_LIST, sourceNameSet)
}

fun String.md5(): String {
    val md5 = MessageDigest.getInstance("MD5")
    return BigInteger(1, md5.digest(toByteArray())).toString(16).padStart(32, '0')
}
