package com.classic.mvloader.memoryCacheManagement


interface CacheManager<T, V> {
    /**
     * clear all items in the cache
     */
    fun clearAll()

    /**
     * return a cached item using its key
     * @param key T
     * @return V?
     */
    fun get(key: T): V?

    /**
     * checks if an item exists in the cache or not
     * @param key T
     * @return Boolean
     */
    fun exists(key: T): Boolean

    /**
     * add an item to the cache
     * @param key T
     * @param value V
     */
    fun set(key: T, value: V)

    /**
     * return the current size of the cache
     * @return Int
     */
    fun size(): Int

    /**
     * change the cache's size
     * @param cacheSize Int
     */
    fun resize(cacheSize: Int)

}