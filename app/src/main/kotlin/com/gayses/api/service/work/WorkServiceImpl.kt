package com.gayses.api.service.work

import com.gayses.api.data.model.*
import com.gayses.api.data.model.Unit
import com.gayses.api.data.repository.*
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
        val workTypeTrim = type.trim()
        val unitTrim = unit?.trim()
        val performerTrim = performer.trim()
        val workTitleTrim = workTypeTrim.trim()
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
        }.let {
            saveWorkInQueue(project, it, expectedItemOrder)
        }
    }

    override fun getWork(project: Project, workId: Long): Work =
        getWorkOrThrow(project, workId)

    override fun getWorkQueue(project: Project): List<WorkQueueItem> =
        workQueueRepository.findByProject_IdOrderByOrderAsc(project.id)

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
            .also {
                val workTypeTrim = type.trim()
                val unitTrim = unit?.trim()
                val performerTrim = performer.trim()
                val workTitleTrim = workTypeTrim.trim()
                val productTitleTrim = productTitle.trim()

                it.type = workTypeRepository
                    .findByTitleIgnoreCase(workTypeTrim)
                    .orElseGet {
                        WorkType(null, workTypeTrim).let {
                            workTypeRepository.save(it)
                        }
                    }

                it.unit = unitTrim?.let {
                    unitRepository
                        .findByTitleIgnoreCase(it)
                        .orElseGet {
                            Unit(null, it).let { model ->
                                unitRepository.save(model)
                            }
                        }
                }

                it.performer = performerRepository
                    .findByTitleIgnoreCase(performerTrim)
                    .orElseGet {
                        Performer(null, performerTrim).let {
                            performerRepository.save(it)
                        }
                    }

                it.workTitle = workTitleTrim
                it.productTitle = productTitleTrim
                it.amount = amount
                it.expectedPaymentDate = expectedPaymentDate?.toInstant()
                it.paymentDate = paymentDate?.toInstant()
                it.expectedDeliveryDate = expectedDeliveryDate?.toInstant()
                it.deliveryDate = deliveryDate?.toInstant()
                it.expectedFinishDate = expectedFinishDate?.toInstant()
                it.finishDate = finishDate?.toInstant()
            }.let {
                workRepository.save(it)
            }

    override fun reorderWorkOrder(project: Project, work: Work, newOrder: Int): WorkQueueItem =
        workQueueRepository
            .findByWork__idAndProject_Id(work.id, project.id)
            .orElseThrow()
            .also { workQueueItem ->
                val queue = getWorkQueue(project)

                val queueWithoutItem = queue.filterNot {
                    it.id == workQueueItem.id
                }

                val actualOrder = getItemOrder(queueWithoutItem, newOrder)

                queue.forEach {
                    if (it.id == workQueueItem.id) {
                        it.order = actualOrder
                    } else {
                        if (it.order >= actualOrder) {
                            it.order += 1
                        }
                    }
                }

                fixWorkItemQueueAndSave(queue)
            }

    override fun deleteWork(project: Project, workId: Long) =
        getWorkOrThrow(project, workId)
            .let {
                workRepository.delete(it)
            }

    private fun getWorkOrThrow(project: Project, id: Long): Work =
        workRepository
            .findBy_idAndQueueItem_Project_Id(id, project.id)
            .orElseThrow {
                ResourceNotFoundException("Work($id) not found")
            }

    private fun saveWorkInQueue(project: Project, work: Work, expectedItemOrder: Int?): WorkQueueItem {
        val queue = workQueueRepository.findByProject_IdOrderByOrderAsc(project.id)
        val actualItemOrder = getItemOrder(queue, expectedItemOrder)

        if (expectedItemOrder != null) {
            queue.forEach {
                if (it.order >= actualItemOrder) {
                    it.order += 1
                }
            }

            fixWorkItemQueueAndSave(queue)
        }

        return WorkQueueItem(
            null,
            work = work,
            order = actualItemOrder,
            project = project
        ).let {
            workQueueRepository.save(it)
        }
    }

    private fun getItemOrder(queue: List<WorkQueueItem>, expectedItemOrder: Int?): Int {
        val maxQueueOrder = queue.maxOfOrNull { it.order } ?: 0

        return if (expectedItemOrder == null || maxQueueOrder <= expectedItemOrder)
            maxQueueOrder + 1
        else
            expectedItemOrder
    }

    private fun fixWorkItemQueueAndSave(queue: List<WorkQueueItem>) {
        val sortedQueue = queue.sortedBy {
            it.order
        }

        if (sortedQueue.isNotEmpty()) {
            val diff = sortedQueue.first().order

            if (diff > 0) {
                sortedQueue.forEach {
                    it.order -= diff
                }
            }

            if (sortedQueue.size > 1) {
                for (i in sortedQueue.indices) {
                    if (i == 0) continue

                    val item = sortedQueue[i]
                    val prevItem = sortedQueue[i - 1]

                    if (item.order - 1 != prevItem.order) {
                        item.order = prevItem.order + 1
                    }
                }
            }

            workQueueRepository.saveAll(sortedQueue)
        }
    }
}