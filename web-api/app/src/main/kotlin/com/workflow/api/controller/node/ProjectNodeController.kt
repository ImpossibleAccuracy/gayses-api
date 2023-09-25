package com.workflow.api.controller.node

import com.workflow.api.controller.node.payload.CreateNodeRequest
import com.workflow.api.controller.node.payload.UpdateNodeRequest
import com.workflow.api.payload.dto.ProjectNodeDto
import com.workflow.api.service.node.ProjectNodeService
import com.workflow.api.service.project.ProjectService
import com.workflow.api.utils.ControllerHelper
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.modelmapper.ModelMapper
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/project/{projectId}/node/")
@PreAuthorize("isAuthenticated()")
@Validated
@SecurityRequirement(name = "bearerAuth")
class ProjectNodeController(
    private val projectService: ProjectService,
    private val projectNodeService: ProjectNodeService,
    private val modelMapper: ModelMapper
) {
    @Operation(summary = "Create task")
    @PostMapping
    fun createNode(
        @PathVariable("projectId") projectId: Long,
        @RequestBody data: CreateNodeRequest
    ): ResponseEntity<ProjectNodeDto> =
        projectService.getProject(projectId, ControllerHelper.account).let { project ->
            val result = projectNodeService.createNode(
                project,
                data.parentNodeId,
                data.type,
                data.workTitle,
                data.productTitle,
                data.unit,
                data.amount,
                data.performer,
                data.expectedPaymentDate,
                data.paymentDate,
                data.expectedDeliveryDate,
                data.deliveryDate,
                data.expectedFinishDate,
                data.finishDate
            )

            val response = modelMapper.map(result, ProjectNodeDto::class.java)

            ResponseEntity.ok(response)
        }

    @Operation(summary = "Get all tasks")
    @GetMapping
    fun getAllWorks(@PathVariable("projectId") projectId: Long): ResponseEntity<List<ProjectNodeDto>> =
        projectService.getProject(projectId, ControllerHelper.account).let { project ->
            val result = projectNodeService.getAllNodes(project)

            val response = result.map {
                modelMapper.map(it, ProjectNodeDto::class.java)
            }

            ResponseEntity.ok(response)
        }

    @Operation(summary = "Update task")
    @PutMapping("/{workId}/")
    fun updateNode(
        @PathVariable("projectId") projectId: Long,
        @PathVariable("workId") workId: Long,
        @RequestBody data: UpdateNodeRequest
    ): ResponseEntity<ProjectNodeDto> =
        projectService.getProject(projectId, ControllerHelper.account).let { project ->
            val result = projectNodeService.updateNode(
                project,
                workId,
                data.parentNodeId,
                data.type,
                data.workTitle,
                data.productTitle,
                data.unit,
                data.amount,
                data.performer,
                data.expectedPaymentDate,
                data.paymentDate,
                data.expectedDeliveryDate,
                data.deliveryDate,
                data.expectedFinishDate,
                data.finishDate
            )

            val response = modelMapper.map(result, ProjectNodeDto::class.java)

            ResponseEntity.ok(response)
        }

    @Operation(summary = "Delete task")
    @DeleteMapping("/{workId}/")
    fun deleteNode(
        @PathVariable("projectId") projectId: Long,
        @PathVariable("workId") workId: Long
    ): ResponseEntity<Nothing> =
        projectService.getProject(projectId, ControllerHelper.account).let { project ->
            projectNodeService.deleteNode(project, workId)

            ResponseEntity.noContent().build()
        }
}
