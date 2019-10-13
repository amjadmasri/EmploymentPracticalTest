package com.classic.mvloader

import android.graphics.Bitmap
import android.os.Build
import android.util.LruCache
import androidx.annotation.RequiresApi


class LRUCacheManager<T>(private var cacheSize: Int) : CacheManager<String, T> {

    private var memoryCache: LruCache<String, T> =
        object : LruCache<String, T>(cacheSize) {
            override fun sizeOf(key: String, value: T): Int {
                return when (value) {
                    is ByteArray -> value.size / 1024
                    is Bitmap -> value.byteCount / 1024
                    else -> throw ClassCastException()
                }
            }
        }

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