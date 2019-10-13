package com.classic.mvloader

import android.os.Build
import android.util.LruCache
import androidx.annotation.RequiresApi

class MVLoaderCacheManager(var cacheSize:Int= maxCacheSize) {

    val imageCache:LRUImageCacheManager= LRUImageCacheManager(cacheSize)
    val fileCache:LRUFileCacheManager= LRUFileCacheManager(cacheSize)

    companion object{
        private val maxCacheSize: Int = (Runtime.getRuntime().maxMemory() / 1024).toInt()/8
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun resize(cacheSize: Int){
        imageCache.resize(cacheSize)
        fileCache.resize(cacheSize)
    }

}