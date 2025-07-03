package com.nazarethlabs.joseph

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JosephApplication

fun main(args: Array<String>) {
    runApplication<JosephApplication>(*args)
}
