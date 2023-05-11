package com.apw.ql

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class ExtKtTest {

    @Test
    fun toTorontoTime_isCorrect() {
        val githubTimeString = "2023-04-11T00:00:00Z"
//        Apr 10, 2023, 8:00:00 PM GMT-4

        val correctResult = "Apr. 10, 2023"

        assertThat(correctResult, `is`(githubTimeString.toDefaultTime()))
    }
}