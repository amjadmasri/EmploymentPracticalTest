package com.classic.mvloader

import android.graphics.Bitmap
import android.os.Build
import android.util.LruCache
import androidx.annotation.RequiresApi


class LRUFileCacheManager (cacheSize: Int): CacheManager<String,ByteArray>  {

    private var cacheSize:Int
    private var memoryCache: LruCache<String, ByteArray>? = null


    init {
        this.cacheSize=cacheSize
        memoryCache=object : LruCache<String, ByteArray>(cacheSize) {
            override fun sizeOf(key: String, byte: ByteArray): Int {
                // The cache size will be measured in kilobytes rather than number of items.
                return byte.size / 1024
            }
        }
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

    override fun exists(key: String): Boolean {
        return memoryCache?.get(key)!=null
    }
}