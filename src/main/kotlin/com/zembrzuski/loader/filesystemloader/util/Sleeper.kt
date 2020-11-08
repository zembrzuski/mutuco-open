package com.zembrzuski.loader.filesystemloader.util

import org.springframework.stereotype.Component

/**
 * Classe reponsável por dar uma dormidinha
 */
@Component
class Sleeper {

    fun sleep(seconds: Int): Boolean {
        Thread.sleep((seconds * 1000).toLong())
        return true
    }

}
