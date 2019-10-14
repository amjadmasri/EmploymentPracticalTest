package demo.mindvalleytest

import kotlin.random.Random

object TestUtil {


    fun randomString(len: Int=10): String {

        val DATA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        var RANDOM = Random(len)

        val sb = StringBuilder(len)

        for (i in 0 until len) {
            sb.append(DATA[RANDOM.nextInt(DATA.length)])
        }

        return sb.toString()

    }

     fun randomNumber(): Double {
        val r = Random(1)
        return (0 + (1 - 0) * r.nextDouble())
    }

    fun randomIntNumber(): Int {
        val r = Random(1)
        return r.nextInt()
    }

}