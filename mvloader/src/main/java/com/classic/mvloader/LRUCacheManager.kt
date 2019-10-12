package com.classic.mvloader

import android.graphics.Bitmap
import android.os.Build
import android.util.LruCache
import androidx.annotation.RequiresApi


class LRUCacheManager (cacheSize: Int=DEFAULT_MAX_CACHE_SIZE): CacheManager  {

    private var cacheSize:Int
    private var memoryCache: LruCache<String, ByteArray>? = null

    companion object{
        const val DEFAULT_MAX_CACHE_SIZE=10
    }

    init {
        this.cacheSize=cacheSize
        memoryCache=LruCache(cacheSize)
    }
    override fun clearAll() {
        memoryCache?.evictAll()
    }

    override fun get(key: String): ByteArray? {
        return memoryCache?.get(key)
    }

    override fun set(key: String, byteArray: ByteArray) {
        memoryCache?.put(key,byteArray)
    }

    override fun size(): Int {
        return memoryCache?.size()!!
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun resize(cacheSize: Int) {
        memoryCache?.resize(cacheSize)
    }
}