package com.classic.mvloader.memoryCacheManagement


interface CacheManager<T, V> {
    fun clearAll()
    fun get(key: T): V?
    fun exists(key: T): Boolean
    fun set(key: T, value: V)
    fun size(): Int
    fun resize(cacheSize: Int)

}