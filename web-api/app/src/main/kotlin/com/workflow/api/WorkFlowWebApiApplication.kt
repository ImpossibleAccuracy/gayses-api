package com.workflow.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WorkFlowWebApiApplication

fun main(args: Array<String>) {
    runApplication<com.workflow.api.WorkFlowWebApiApplication>(*args)
}
