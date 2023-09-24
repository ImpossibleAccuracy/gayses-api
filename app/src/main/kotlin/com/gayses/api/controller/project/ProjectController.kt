package com.gayses.api.controller.project

import com.gayses.api.controller.project.payload.CreateProjectRequest
import com.gayses.api.controller.project.payload.UpdateProjectRequest
import com.gayses.api.payload.dto.ProjectDto
import com.gayses.api.service.project.ProjectService
import com.gayses.api.utils.ControllerHelper
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.modelmapper.ModelMapper
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/project")
@PreAuthorize("isAuthenticated()")
@Validated
@SecurityRequirement(name = "bearerAuth")
class ProjectController(
    private val projectService: ProjectService,
    private val modelMapper: ModelMapper
) {
    @Operation(summary = "Create project")
    @PostMapping
    fun createProject(@RequestBody data: CreateProjectRequest): ResponseEntity<ProjectDto> =
        projectService.createProject(ControllerHelper.account, data.title).let {
            val response = modelMapper.map(it, ProjectDto::class.java)

            ResponseEntity.ok(response)
        }

    @Operation(summary = "Get all projects")
    @GetMapping
    fun getAllProjects(): ResponseEntity<List<ProjectDto>> =
        projectService.getAllProjects(ControllerHelper.account)
            .map {
                modelMapper.map(it, ProjectDto::class.java)
            }
            .let {
                ResponseEntity.ok(it)
            }

    @Operation(summary = "Get project by id")
    @GetMapping("/{id}")
    fun getProjectById(@PathVariable("id") id: Long): ResponseEntity<ProjectDto> =
        projectService.getProject(id, ControllerHelper.account)
            .let {
                val response = modelMapper.map(it, ProjectDto::class.java)

                ResponseEntity.ok(response)
            }

    @Operation(summary = "Update project")
    @PutMapping("/{id}")
    fun updateProjectById(
        @PathVariable("id") id: Long,
        @RequestBody data: UpdateProjectRequest
    ): ResponseEntity<ProjectDto> =
        projectService.updateProject(id, ControllerHelper.account, data.title)
            .let {
                val response = modelMapper.map(it, ProjectDto::class.java)

                ResponseEntity.ok(response)
            }

    @Operation(summary = "Delete project")
    @DeleteMapping("/{id}")
    fun updateProjectById(@PathVariable("id") id: Long): ResponseEntity<Nothing> =
        projectService.deleteProject(id, ControllerHelper.account)
            .let {
                ResponseEntity.noContent().build()
            }
}