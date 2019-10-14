package com.classic.mvloader.memoryCacheManagement

import android.os.Build
import android.util.LruCache
import androidx.annotation.RequiresApi


class LRUCacheManager<T>(private var memoryCache: LruCache<String, T>) :
    CacheManager<String, T> {

    override fun clearAll() {
        memoryCache.evictAll()
    }

    override fun get(key: String): T? {
        return memoryCache.get(key)
    }

    override fun set(key: String, value: T) {
        memoryCache.put(key, value)
    }

    override fun size(): Int {
        return memoryCache.size()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun resize(cacheSize: Int) {
        memoryCache.resize(cacheSize)
    }

    override fun exists(key: String): Boolean {
        return memoryCache.get(key) != null
    }
}