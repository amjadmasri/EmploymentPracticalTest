package com.classic.mvloader.utilities

/**
 * a utility class that allows the ability to create a singleton instance of classes that extend this utility
 * the singleton can take one argument as a parameter
 * @param out T : Any
 * @param in A
 * @property creator Function1<A, T>?
 * @property instance T?
 * @constructor
 */
open class SingletonHolder<out T : Any, in A>(creator: (A) -> T) {
    private var creator: ((A) -> T)? = creator
    @Volatile
    private var instance: T? = null

    fun getInstance(arg: A): T {
        val i = instance
        if (i != null) {
            return i
        }

        return synchronized(this) {
            val i2 = instance
            if (i2 != null) {
                i2
            } else {
                val created = creator!!(arg)
                instance = created
                creator = null
                created
            }
        }
    }
}