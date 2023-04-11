package com.example.displaygu

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith

class ExtKtTest {

    @Test
    fun toTorontoTime_isCorrect() {
        val githubTimeString = "2023-04-11T00:00:00Z"
//        Apr 10, 2023, 8:00:00 PM
        val correctResult = "2023-04-10 20:00"

        assertThat(correctResult, `is`(githubTimeString.toTorontoTime()))
    }
}