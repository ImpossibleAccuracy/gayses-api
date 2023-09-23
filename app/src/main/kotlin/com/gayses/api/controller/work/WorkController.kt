package com.gayses.api.controller.work

import com.gayses.api.controller.work.payload.CreateWorkRequest
import com.gayses.api.controller.work.payload.UpdateWorkRequest
import com.gayses.api.payload.dto.WorkDto
import com.gayses.api.payload.dto.WorkQueueItemDto
import com.gayses.api.service.project.ProjectService
import com.gayses.api.service.work.WorkService
import com.gayses.api.utils.ControllerHelper
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.modelmapper.ModelMapper
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/project/{projectId}/work")
@PreAuthorize("isAuthenticated()")
@SecurityRequirement(name = "bearerAuth")
class WorkController(
    private val projectService: ProjectService,
    private val workService: WorkService,
    private val modelMapper: ModelMapper
) {
    @Operation(summary = "Create task")
    @PostMapping
    fun createWork(
        @PathVariable("projectId") projectId: Long,
        @RequestBody data: @Valid CreateWorkRequest
    ): ResponseEntity<WorkQueueItemDto> =
        projectService.getProject(projectId, ControllerHelper.account).let { project ->
            val workQueueItem = workService.createWork(
                project,
                data.order,
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

            val response = modelMapper.map(workQueueItem, WorkQueueItemDto::class.java)

            ResponseEntity.ok(response)
        }

    @Operation(summary = "Get all tasks")
    @GetMapping
    fun getAllWorks(@PathVariable("projectId") projectId: Long): ResponseEntity<List<WorkQueueItemDto>> =
        projectService.getProject(projectId, ControllerHelper.account).let { project ->
            val data = workService.getWorkQueue(project).map {
                modelMapper.map(it, WorkQueueItemDto::class.java)
            }

            ResponseEntity.ok(data)
        }

    @Operation(summary = "Update task")
    @PutMapping("/{workId}")
    fun updateWork(
        @PathVariable("projectId") projectId: Long,
        @PathVariable("workId") workId: Long,
        @RequestBody data: @Valid UpdateWorkRequest
    ): ResponseEntity<WorkDto> =
        projectService.getProject(projectId, ControllerHelper.account).let { project ->
            val work = workService.updateWork(
                project,
                workId,
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

            val response = modelMapper.map(work, WorkDto::class.java)

            ResponseEntity.ok(response)
        }

    @Operation(summary = "Reorder task in project queue")
    @PutMapping("/{workId}/reorder")
    fun reorderWork(
        @PathVariable("projectId") projectId: Long,
        @PathVariable("workId") workId: Long,
        @RequestParam("new_order") newOrder: Int
    ): ResponseEntity<WorkQueueItemDto> =
        projectService.getProject(projectId, ControllerHelper.account).let { project ->
            val work = workService.getWork(project, workId)

            val queueItem = workService.reorderWorkOrder(project, work, newOrder)

            val response = modelMapper.map(queueItem, WorkQueueItemDto::class.java)

            ResponseEntity.ok(response)
        }

    @Operation(summary = "Delete task")
    @DeleteMapping("/{workId}")
    fun deleteWork(
        @PathVariable("projectId") projectId: Long,
        @PathVariable("workId") workId: Long
    ): ResponseEntity<Nothing> =
        projectService.getProject(projectId, ControllerHelper.account).let { project ->
            workService.deleteWork(project, workId)

            ResponseEntity.noContent().build()
        }
}
