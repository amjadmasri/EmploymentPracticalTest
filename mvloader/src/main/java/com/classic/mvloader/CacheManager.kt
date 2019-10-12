package com.classic.mvloader


interface CacheManager {
    fun clearAll()
    fun get(key:String):ByteArray?
    fun set(key: String, byteArray: ByteArray)
    fun size(): Int
    fun resize(cacheSize:Int)

}