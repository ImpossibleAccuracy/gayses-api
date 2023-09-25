package com.workflow.api.service.node

import com.workflow.api.data.model.*
import com.workflow.api.data.repository.ProjectNodeItemRepository
import com.workflow.api.data.repository.ProjectNodePerformerRepository
import com.workflow.api.data.repository.ProjectNodeTypeRepository
import com.workflow.api.data.repository.ProjectNodeUnitRepository
import com.workflow.api.exception.InvalidServiceArguments
import com.workflow.api.exception.ResourceNotFoundException
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProjectNodeServiceImpl(
    private val projectNodeTypeRepository: ProjectNodeTypeRepository,
    private val projectNodeUnitRepository: ProjectNodeUnitRepository,
    private val projectNodePerformerRepository: ProjectNodePerformerRepository,
    private val projectNodeItemRepository: ProjectNodeItemRepository
) : ProjectNodeService {
    override fun createNode(
        project: Project,
        parentNodeId: Long?,
        type: String,
        workTitle: String,
        productTitle: String,
        unit: String?,
        amount: Int,
        performer: String,
        expectedPaymentDate: Date?,
        paymentDate: Date?,
        expectedDeliveryDate: Date?,
        deliveryDate: Date?,
        expectedFinishDate: Date?,
        finishDate: Date?
    ): ProjectNodeItem {
        validateData(type, workTitle, productTitle, unit, amount, performer)

        val workTypeTrim = type.trim()
        val unitTrim = unit?.trim()
        val performerTrim = performer.trim()
        val workTitleTrim = workTitle.trim()
        val productTitleTrim = productTitle.trim()

        val projectNodeTypeModel = projectNodeTypeRepository
            .findByTitleIgnoreCase(workTypeTrim)
            .orElseGet {
                ProjectNodeType(null, workTypeTrim).let {
                    projectNodeTypeRepository.save(it)
                }
            }

        val projectNodeUnitModel = unitTrim?.let {
            projectNodeUnitRepository
                .findByTitleIgnoreCase(it)
                .orElseGet {
                    ProjectNodeUnit(null, it).let { model ->
                        projectNodeUnitRepository.save(model)
                    }
                }
        }

        val performerModel = projectNodePerformerRepository
            .findByTitleIgnoreCase(performerTrim)
            .orElseGet {
                Performer(null, performerTrim).let {
                    projectNodePerformerRepository.save(it)
                }
            }

        val parentNode = parentNodeId?.let {
            getNodeOrThrow(project, it)
        }

        return ProjectNodeItem(
            id = null,
            type = projectNodeTypeModel,
            workTitle = workTitleTrim,
            productTitle = productTitleTrim,
            projectNodeUnit = projectNodeUnitModel,
            amount = amount,
            performer = performerModel,
            expectedPaymentDate = expectedPaymentDate?.toInstant(),
            paymentDate = paymentDate?.toInstant(),
            expectedDeliveryDate = expectedDeliveryDate?.toInstant(),
            deliveryDate = deliveryDate?.toInstant(),
            expectedFinishDate = expectedFinishDate?.toInstant(),
            finishDate = finishDate?.toInstant(),
            project = project,
            parentNode = parentNode
        ).let {
            projectNodeItemRepository.save(it)
        }
    }

    override fun getNode(project: Project, nodeId: Long): ProjectNodeItem =
        getNodeOrThrow(project, nodeId)

    override fun getAllNodes(project: Project): List<ProjectNodeItem> =
        projectNodeItemRepository.findByProject_IdOrderByCreatedAtAscParentNode_CreatedAtAsc(project.id)

    override fun updateNode(
        project: Project,
        nodeId: Long,
        parentNodeId: Long?,
        type: String,
        workTitle: String,
        productTitle: String,
        unit: String?,
        amount: Int,
        performer: String,
        expectedPaymentDate: Date?,
        paymentDate: Date?,
        expectedDeliveryDate: Date?,
        deliveryDate: Date?,
        expectedFinishDate: Date?,
        finishDate: Date?
    ): ProjectNodeItem =
        getNodeOrThrow(project, nodeId)
            .also { work ->
                validateData(type, workTitle, productTitle, unit, amount, performer)

                val workTypeTrim = type.trim()
                val unitTrim = unit?.trim()
                val performerTrim = performer.trim()
                val workTitleTrim = workTitle.trim()
                val productTitleTrim = productTitle.trim()

                work.type = projectNodeTypeRepository
                    .findByTitleIgnoreCase(workTypeTrim)
                    .orElseGet {
                        ProjectNodeType(null, workTypeTrim).let {
                            projectNodeTypeRepository.save(it)
                        }
                    }

                work.projectNodeUnit = unitTrim?.let {
                    projectNodeUnitRepository
                        .findByTitleIgnoreCase(it)
                        .orElseGet {
                            ProjectNodeUnit(null, it).let { model ->
                                projectNodeUnitRepository.save(model)
                            }
                        }
                }

                work.performer = projectNodePerformerRepository
                    .findByTitleIgnoreCase(performerTrim)
                    .orElseGet {
                        Performer(null, performerTrim).let {
                            projectNodePerformerRepository.save(it)
                        }
                    }

                work.parentNode = parentNodeId?.let {
                    getNodeOrThrow(project, it)
                }

                work.workTitle = workTitleTrim
                work.productTitle = productTitleTrim
                work.amount = amount
                work.expectedPaymentDate = expectedPaymentDate?.toInstant()
                work.paymentDate = paymentDate?.toInstant()
                work.expectedDeliveryDate = expectedDeliveryDate?.toInstant()
                work.deliveryDate = deliveryDate?.toInstant()
                work.expectedFinishDate = expectedFinishDate?.toInstant()
                work.finishDate = finishDate?.toInstant()
            }.let {
                projectNodeItemRepository.save(it)
            }

    override fun deleteNode(project: Project, nodeId: Long) =
        getNodeOrThrow(project, nodeId).let {
            projectNodeItemRepository.delete(it)
        }

    private fun getNodeOrThrow(project: Project, id: Long): ProjectNodeItem =
        projectNodeItemRepository
            .findByIdAndProject_Id(id, project.id)
            .orElseThrow {
                ResourceNotFoundException("Work($id) not found")
            }

    private fun validateData(
        type: String,
        workTitle: String,
        productTitle: String,
        unit: String?,
        amount: Int,
        performer: String
    ) {
        if (type.isBlank()) {
            throw InvalidServiceArguments("Work type cannot be blank")
        }

        if (workTitle.isBlank()) {
            throw InvalidServiceArguments("Work's project title cannot be blank")
        }

        if (productTitle.isBlank()) {
            throw InvalidServiceArguments("Work's product title cannot be blank")
        }

        if (unit != null && unit.isBlank()) {
            throw InvalidServiceArguments("Unit cannot be blank")
        }

        if (amount <= 0) {
            throw InvalidServiceArguments("Product project's lower or equals than zero")
        }

        if (performer.isBlank()) {
            throw InvalidServiceArguments("Work's performer cannot be blank")
        }
    }


    /*

    override fun reorderWorkOrder(project: Project, work: ProjectNodeItem, newOrder: Int): WorkQueueItem =
        workQueueRepository
            .existsByWork__idAndProject_Id(work.id, project.id)
            .let {
                if (!it) {
                    throw ResourceNotFoundException("WorkQueueItem with WorkId=${work.id} not found")
                }
            }
            .let {
                if (newOrder < 0) {
                    throw InvalidServiceArguments("Work's order cannot be lower then zero")
                }

                val workItems = getWorkItemsSorted(project).filterNot {
                    it.id == work.id
                }.toMutableList()

                val actualOrder = getActualItemOrder(workItems, newOrder)

                if (actualOrder > workItems.size) {
                    workItems.add(work)
                } else {
                    workItems.add(actualOrder, work)
                }

                saveWorkQueueItems(project, workItems).first {
                    it.work.id == work.id
                }
            }

    override fun deleteWork(project: Project, workId: Long) =
        getWorkOrThrow(project, workId)
            .let {
                workRepository.delete(it)
            }.also {
                saveWorkQueueItems(project)
            }

    private fun getActualItemOrder(items: List<ProjectNodeItem>, expectedItemOrder: Int?): Int {
        return when {
            (items.isEmpty()) -> 0
            (expectedItemOrder == null || items.size <= expectedItemOrder) -> (items.size + 1)
            else -> expectedItemOrder
        }
    }

    private fun getWorkQueueItemsSorted(project: Project) =
        workQueueRepository.findByProject_IdOrderByOrderAsc(project.id)

    private fun getWorkItemsSorted(project: Project) =
        getWorkQueueItemsSorted(project).map {
            it.work
        }

    private fun saveWorkQueueItems(project: Project): List<WorkQueueItem> =
        saveWorkQueueItems(getWorkQueueItemsSorted(project))

    private fun saveWorkQueueItems(project: Project, workItems: List<ProjectNodeItem>): List<WorkQueueItem> =
        getWorkQueueItemsSorted(project).let { queue ->
            val queueItems = workItems.map { work ->
                queue.find {
                    it.work.id == work.id
                }
                    ?: WorkQueueItem(
                        id = null,
                        project = project,
                        work = work,
                        order = -1
                    )
            }

            saveWorkQueueItems(queueItems)
        }


    private fun saveWorkQueueItems(queueItems: List<WorkQueueItem>): List<WorkQueueItem> =
        queueItems.let {
            it.forEachIndexed { i, item ->
                item.order = i
            }

            workQueueRepository.saveAll(it)
        }*/
}