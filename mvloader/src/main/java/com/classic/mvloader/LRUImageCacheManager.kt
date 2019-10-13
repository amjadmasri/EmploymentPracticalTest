package com.classic.mvloader

import android.graphics.Bitmap
import android.os.Build
import android.util.LruCache
import androidx.annotation.RequiresApi


class LRUImageCacheManager (cacheSize: Int): CacheManager<String,Bitmap>  {

    private var cacheSize:Int
    private var memoryCache: LruCache<String, Bitmap>? = null

    init {
        this.cacheSize=cacheSize
        memoryCache=object : LruCache<String, Bitmap>(cacheSize) {
            override fun sizeOf(key: String, bitmap: Bitmap): Int {
                // The cache size will be measured in kilobytes rather than number of items.
                return bitmap.byteCount / 1024
            }
        }
    }
    override fun clearAll() {
        memoryCache?.evictAll()
    }

    override fun get(key: String): Bitmap? {
        return memoryCache?.get(key)
    }

    override fun set(key: String, byteArray: Bitmap) {
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