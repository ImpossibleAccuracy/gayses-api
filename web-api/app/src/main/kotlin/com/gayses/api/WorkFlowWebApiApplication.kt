package com.gayses.api

import com.gayses.api.service.auth.AuthService
import com.gayses.api.service.node.ProjectNodeService
import com.gayses.api.service.project.ProjectService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WorkFlowWebApiApplication

fun main(args: Array<String>) {
    val ctx = runApplication<WorkFlowWebApiApplication>(*args)

    val authService = ctx.getBean(AuthService::class.java)
    val projectService = ctx.getBean(ProjectService::class.java)
    val projectNodeService = ctx.getBean(ProjectNodeService::class.java)

    val account = authService.registration("em", "pa").account
    val project = projectService.createProject(account, "Test project")

    val node = projectNodeService.createNode(
        project,
        null,
        "Type",
        "Work title",
        "Project title",
        "mm",
        23,
        "Performer",
        null,
        null,
        null,
        null,
        null,
        null
    )

    val node2 = projectNodeService.createNode(
        project,
        node.id,
        "Type",
        "Work title#2",
        "Project title#2",
        "mm",
        23,
        "Performer",
        null,
        null,
        null,
        null,
        null,
        null
    )

    val node3 = projectNodeService.createNode(
        project,
        node2.id,
        "Type",
        "Work title#3",
        "Project title#3",
        "mm",
        23,
        "Performer",
        null,
        null,
        null,
        null,
        null,
        null
    )

    val node4 = projectNodeService.createNode(
        project,
        node.id,
        "Type",
        "Work title#4",
        "Project title#4",
        "mm",
        23,
        "Performer",
        null,
        null,
        null,
        null,
        null,
        null
    )

    val node5 = projectNodeService.createNode(
        project,
        null,
        "Type",
        "Work title#5",
        "Project title#5",
        "mm",
        23,
        "Performer",
        null,
        null,
        null,
        null,
        null,
        null
    )

}
