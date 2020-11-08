package com.zembrzuski.loader.filesystemloader.util

import org.springframework.stereotype.Component
import kotlin.streams.asSequence

@Component
class RandomNameGenerator {

    private val source = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

    fun randomName(): String {
        return java.util.Random().ints(10, 0, source.length)
                .asSequence()
                .map(source::get)
                .joinToString("")
    }

}