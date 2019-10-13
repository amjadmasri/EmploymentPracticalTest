package com.classic.mvloader

import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi

class MVLoaderCacheManager(var cacheSize: Int = MAX_CACHE_SIZE) {

    val imageCache: LRUCacheManager<Bitmap> = LRUCacheManager(cacheSize)
    val cache: LRUCacheManager<ByteArray> = LRUCacheManager(cacheSize)

    companion object {
        private val MAX_CACHE_SIZE: Int = (Runtime.getRuntime().maxMemory() / 1024).toInt() / 8
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun resize(cacheSize: Int) {
        imageCache.resize(cacheSize)
        cache.resize(cacheSize)
    }

}