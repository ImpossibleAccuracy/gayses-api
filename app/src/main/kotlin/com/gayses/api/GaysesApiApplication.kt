package com.gayses.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
class GaysesApiApplication

fun main(args: Array<String>) {
    runApplication<GaysesApiApplication>(*args)
}
