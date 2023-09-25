package com.gayses.api.service.work

import com.gayses.api.data.model.*
import com.gayses.api.data.model.Unit
import com.gayses.api.data.repository.*
import com.gayses.api.exception.InvalidServiceArguments
import com.gayses.api.exception.ResourceNotFoundException
import org.springframework.stereotype.Service
import java.util.*

@Service
class WorkServiceImpl(
    private val workTypeRepository: WorkTypeRepository,
    private val unitRepository: UnitRepository,
    private val performerRepository: PerformerRepository,
    private val workQueueRepository: WorkQueueRepository,
    private val workRepository: WorkRepository
) : WorkService {
    override fun createWork(
        project: Project,
        expectedItemOrder: Int?,
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
    ): WorkQueueItem {
        validateData(type, workTitle, productTitle, unit, amount, performer)

        if (expectedItemOrder != null && expectedItemOrder < 0) {
            throw InvalidServiceArguments("Work's order cannot be lower then zero")
        }

        val workTypeTrim = type.trim()
        val unitTrim = unit?.trim()
        val performerTrim = performer.trim()
        val workTitleTrim = workTitle.trim()
        val productTitleTrim = productTitle.trim()

        val workTypeModel = workTypeRepository
            .findByTitleIgnoreCase(workTypeTrim)
            .orElseGet {
                WorkType(null, workTypeTrim).let {
                    workTypeRepository.save(it)
                }
            }

        val unitModel = unitTrim?.let {
            unitRepository
                .findByTitleIgnoreCase(it)
                .orElseGet {
                    Unit(null, it).let { model ->
                        unitRepository.save(model)
                    }
                }
        }

        val performerModel = performerRepository
            .findByTitleIgnoreCase(performerTrim)
            .orElseGet {
                Performer(null, performerTrim).let {
                    performerRepository.save(it)
                }
            }

        return Work(
            id = null,
            type = workTypeModel,
            workTitle = workTitleTrim,
            productTitle = productTitleTrim,
            unit = unitModel,
            amount = amount,
            performer = performerModel,
            expectedPaymentDate = expectedPaymentDate?.toInstant(),
            paymentDate = paymentDate?.toInstant(),
            expectedDeliveryDate = expectedDeliveryDate?.toInstant(),
            deliveryDate = deliveryDate?.toInstant(),
            expectedFinishDate = expectedFinishDate?.toInstant(),
            finishDate = finishDate?.toInstant()
        ).let {
            workRepository.save(it)
        }.let { work ->
            val workItems = getWorkItemsSorted(project).toMutableList()

            val actualOrder = getActualItemOrder(workItems, expectedItemOrder)

            if (actualOrder > workItems.size) {
                workItems.add(work)
            } else {
                workItems.add(actualOrder, work)
            }

            saveWorkQueueItems(project, workItems).first {
                it.work.id == work.id
            }
        }
    }

    override fun getWork(project: Project, workId: Long): Work =
        getWorkOrThrow(project, workId)

    override fun getWorkQueue(project: Project): List<WorkQueueItem> =
        getWorkQueueItemsSorted(project)

    override fun updateWork(
        project: Project,
        workId: Long,
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
    ): Work =
        getWorkOrThrow(project, workId)
            .also { work ->
                validateData(type, workTitle, productTitle, unit, amount, performer)

                val workTypeTrim = type.trim()
                val unitTrim = unit?.trim()
                val performerTrim = performer.trim()
                val workTitleTrim = workTitle.trim()
                val productTitleTrim = productTitle.trim()

                work.type = workTypeRepository
                    .findByTitleIgnoreCase(workTypeTrim)
                    .orElseGet {
                        WorkType(null, workTypeTrim).let {
                            workTypeRepository.save(it)
                        }
                    }

                work.unit = unitTrim?.let {
                    unitRepository
                        .findByTitleIgnoreCase(it)
                        .orElseGet {
                            Unit(null, it).let { model ->
                                unitRepository.save(model)
                            }
                        }
                }

                work.performer = performerRepository
                    .findByTitleIgnoreCase(performerTrim)
                    .orElseGet {
                        Performer(null, performerTrim).let {
                            performerRepository.save(it)
                        }
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
                workRepository.save(it)
            }

    override fun reorderWorkOrder(project: Project, work: Work, newOrder: Int): WorkQueueItem =
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

    private fun getWorkOrThrow(project: Project, id: Long): Work =
        workRepository
            .findBy_idAndQueueItem_Project_Id(id, project.id)
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

    private fun getActualItemOrder(items: List<Work>, expectedItemOrder: Int?): Int {
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

    private fun saveWorkQueueItems(project: Project, workItems: List<Work>): List<WorkQueueItem> =
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
        }
}