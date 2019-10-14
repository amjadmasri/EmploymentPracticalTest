package com.classic.mvloader

import android.util.LruCache
import com.classic.mvloader.memoryCacheManagement.LRUCacheManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LRUCacheManagerTest {

    private fun <T> anyObject(): T {
        return Mockito.any<T>()
    }

    private lateinit var lruCacheManager: LRUCacheManager<ByteArray>

    @Mock
    lateinit var lruCache: LruCache<String, ByteArray>

    var expectedByteArray = ByteArray(10)
    var unExpectedByteArray = ByteArray(9)

    @Before
    fun setupLRUCacheManager() {
        mock(LruCache::class.java)
        lruCacheManager = LRUCacheManager(lruCache)

        setupMocks()
    }

    private fun setupMocks() {

        Mockito.`when`(lruCache.get("item1")).thenReturn(expectedByteArray)

        Mockito.`when`(lruCache.get("item5")).thenReturn(null)
        Mockito.`when`(lruCache.get("item6")).thenReturn(expectedByteArray)

        Mockito.`when`(lruCache.evictAll()).thenCallRealMethod()
        Mockito.`when`(lruCache.evictAll()).thenAnswer {
            Mockito.`when`(lruCache.size()).thenReturn(0)
            Mockito.`when`(lruCache.get(ArgumentMatchers.anyString())).thenReturn(null)
        }

        Mockito.`when`(lruCache.put("item2", expectedByteArray)).thenAnswer {
            Mockito.`when`(lruCache.get("item2")).thenReturn(expectedByteArray)
        }

        Mockito.`when`(lruCache.resize(10)).thenAnswer {
            Mockito.`when`(lruCache.size()).thenReturn(10)
        }

        Mockito.`when`(lruCache.put("item7", expectedByteArray)).thenAnswer {
            Mockito.`when`(lruCache.size()).thenReturn(15)
        }
    }

    @Test
    fun clearAll() {
        lruCacheManager.clearAll()
        assertEquals(0, lruCache.size())
    }

    @Test
    fun clearAll_check_value_null() {
        lruCacheManager.clearAll()
        assertEquals(null, lruCache.get(ArgumentMatchers.anyString()))
    }

    @Test
    fun get() {
        assertEquals(lruCacheManager.get("item1")?.size, 10)
    }

    @Test
    fun set() {
        lruCacheManager.set("item2", expectedByteArray)

        assertEquals(lruCacheManager.get("item2"), expectedByteArray)
    }

    @Test
    fun set_failure() {
        lruCacheManager.set("item2", expectedByteArray)

        assertNotEquals(lruCacheManager.get("item2"), unExpectedByteArray)
    }

    @Test
    fun size() {
        lruCacheManager.set("item7", expectedByteArray)

        assertEquals(lruCacheManager.size(), 15)
    }

    @Test
    fun resize() {
        lruCacheManager.resize(10)

        assertEquals(lruCacheManager.size(), 10)
    }

    @Test
    fun resize_check_not_equal() {
        lruCacheManager.resize(10)

        assertNotEquals(lruCacheManager.size(), 8)
    }


    @Test
    fun check_exists() {
        assertEquals(lruCacheManager.exists("item6"), true)

    }

    @Test
    fun check_not_exists() {
        assertEquals(lruCacheManager.exists("item5"), false)
    }
}