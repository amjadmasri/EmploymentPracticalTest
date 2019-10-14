package com.classic.mvloader

import android.graphics.Bitmap
import android.util.LruCache
import com.classic.mvloader.memoryCacheManagement.LRUCacheManager
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class LRUCacheManagerInvocationTest {

    private fun <T> anyObject(): T {
        return Mockito.any<T>()
    }

    private lateinit var lruCacheManager: LRUCacheManager<Bitmap>

    @Mock
    lateinit var lruCache: LruCache<String, Bitmap>

    @Before
    fun setupLRUCacheManager() {
        mock(LruCache::class.java)
        lruCacheManager = LRUCacheManager(lruCache)

    }

    @Test
    fun test_correct_size_invocation() {

        lruCacheManager.size()

        verify(lruCache).size()
    }

    @Test
    fun test_correct_clear_invocation() {

        lruCacheManager.clearAll()

        verify(lruCache).evictAll()
    }

    @Test
    fun test_correct_get_invocation() {

        lruCacheManager.get(ArgumentMatchers.anyString())

        verify(lruCache).get(ArgumentMatchers.anyString())
    }

    @Test
    fun test_correct_set_invocation() {

        lruCacheManager.set(ArgumentMatchers.anyString(), anyObject())

        verify(lruCache).put(ArgumentMatchers.anyString(), anyObject())
    }

    @Test
    fun test_correct_resize_invocation() {

        lruCacheManager.resize(ArgumentMatchers.anyInt())

        verify(lruCache).resize(ArgumentMatchers.anyInt())
    }

    @Test
    fun test_correct_exists_invocation() {

        lruCacheManager.exists(ArgumentMatchers.anyString())

        verify(lruCache).get(ArgumentMatchers.anyString())
    }

}