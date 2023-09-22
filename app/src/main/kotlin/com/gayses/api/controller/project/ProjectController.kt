package com.gayses.api.controller.project

import com.gayses.api.controller.project.payload.CreateProjectRequest
import com.gayses.api.payload.dto.ProjectDto
import com.gayses.api.service.project.ProjectService
import com.gayses.api.utils.ControllerHelper
import jakarta.validation.Valid
import org.modelmapper.ModelMapper
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/project")
class ProjectController(
    private val projectService: ProjectService,
    private val modelMapper: ModelMapper
) {
    @Secured
    @PostMapping
    fun createProject(@RequestBody data: @Valid CreateProjectRequest): ResponseEntity<ProjectDto> =
        projectService.createProject(data.title, ControllerHelper.account).let {
            val response = modelMapper.map(it, ProjectDto::class.java)

            ResponseEntity.ok(response)
        }
}