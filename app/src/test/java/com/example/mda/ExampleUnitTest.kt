package com.example.mda

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val a = listOf(1,2,3,4)
        val b = listOf(2,3, 5)
        val c = listOf(4, 5, 3)

        println(a.intersect(b).intersect(c))
    }
}