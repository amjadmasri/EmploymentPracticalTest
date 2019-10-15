package com.classic.mvloader.memoryCacheManagement

import android.graphics.Bitmap
import android.os.Build
import android.util.LruCache
import androidx.annotation.RequiresApi

/**
 * A holder object for two types of memory caches
 * Image memory cache that holds the images decoded and scaled bitmaps
 * File memory cache that holds the files as their ByteArray representation
 * @property maxCacheCapacity Int
 * @property imageCache LRUCacheManager<Bitmap>
 * @property cache LRUCacheManager<ByteArray>
 * @property imageMemoryCache LruCache<String, Bitmap>
 * @property resourceMemoryCache LruCache<String, ByteArray>
 * @constructor
 */
class MVLoaderCacheManager(var maxCacheCapacity: Int = MAX_CACHE_CAPACITY) {

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
            object : LruCache<String, Bitmap>(maxCacheCapacity) {
                override fun sizeOf(key: String, value: Bitmap): Int {
                    return value.byteCount / 1024

                }
            }

        resourceMemoryCache =
            object : LruCache<String, ByteArray>(maxCacheCapacity) {
                override fun sizeOf(key: String, value: ByteArray): Int {
                    return value.size / 1024

                }
            }

    }

    companion object {
         val MAX_CACHE_CAPACITY: Int = (Runtime.getRuntime().maxMemory() / 1024).toInt() / 8
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun resize(maxCacheCapacity: Int) {
        imageCache.resize(maxCacheCapacity)
        cache.resize(maxCacheCapacity)
    }

}