package com.gayses.api.controller.project

import com.gayses.api.controller.project.payload.CreateProjectRequest
import com.gayses.api.controller.project.payload.CreateProjectResponse
import com.gayses.api.service.project.ProjectService
import com.gayses.api.utils.ControllerHelper
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/project")
class ProjectController(
    private val projectService: ProjectService
) {
    @Secured
    @PostMapping
    fun createProject(@RequestBody data: @Valid CreateProjectRequest): ResponseEntity<CreateProjectResponse> =
        projectService.createProject(data.title, ControllerHelper.account).let {
            val response = CreateProjectResponse(true)

            ResponseEntity.ok(response)
        }
}