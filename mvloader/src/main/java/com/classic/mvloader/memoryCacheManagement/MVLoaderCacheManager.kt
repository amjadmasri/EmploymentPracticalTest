package com.classic.mvloader.memoryCacheManagement

import android.graphics.Bitmap
import android.os.Build
import android.util.LruCache
import androidx.annotation.RequiresApi

/**
 * A holder object for two types of memory caches
 * Image memory cache that holds the images decoded and scaled bitmaps
 * File memory cache that holds the files as their ByteArray representation
 * @property cacheSize Int
 * @property imageCache LRUCacheManager<Bitmap>
 * @property cache LRUCacheManager<ByteArray>
 * @property imageMemoryCache LruCache<String, Bitmap>
 * @property resourceMemoryCache LruCache<String, ByteArray>
 * @constructor
 */
class MVLoaderCacheManager(var cacheSize: Int = MAX_CACHE_SIZE) {

    val imageCache: LRUCacheManager<Bitmap> by lazy {
        LRUCacheManager(
            imageMemoryCache
        )
    }
    val cache: LRUCacheManager<ByteArray> by lazy {
        LRUCacheManager(
            resourceMemoryCache
        )
    }

    private var imageMemoryCache: LruCache<String, Bitmap>
    private var resourceMemoryCache: LruCache<String, ByteArray>

    init {
        imageMemoryCache =
            object : LruCache<String, Bitmap>(cacheSize) {
                override fun sizeOf(key: String, value: Bitmap): Int {
                    return value.byteCount / 1024

                }
            }

        resourceMemoryCache =
            object : LruCache<String, ByteArray>(cacheSize) {
                override fun sizeOf(key: String, value: ByteArray): Int {
                    return value.size / 1024

                }
            }

    }

    companion object {
        private val MAX_CACHE_SIZE: Int = (Runtime.getRuntime().maxMemory() / 1024).toInt() / 8
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun resize(cacheSize: Int) {
        imageCache.resize(cacheSize)
        cache.resize(cacheSize)
    }

}