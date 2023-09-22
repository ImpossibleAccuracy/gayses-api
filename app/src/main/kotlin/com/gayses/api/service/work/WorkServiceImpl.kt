package com.gayses.api.service.work

import com.gayses.api.data.model.*
import com.gayses.api.data.model.Unit
import com.gayses.api.data.repository.*
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
        order: Int,
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
        finishDate: Date?,
    ): Work {
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

        val work = Work(
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
        }

        val actualQueue = workQueueRepository.findByProject_IdOrderByOrderAsc(project.id)
        val newItemOrder = getNewQueueItemOrder(actualQueue, order)

        shiftQueue(actualQueue, order)

        WorkQueueItem(
            null,
            work = work,
            order = newItemOrder,
            project = project
        ).let {
            workQueueRepository.save(it)
        }

        return work
    }

    private fun shiftQueue(queue: List<WorkQueueItem>, newItemOrder: Int) {
        val itemsToUpdate = queue
            .filter {
                it.order >= newItemOrder
            }

        itemsToUpdate.forEach {
            it.order += 1
        }

        workQueueRepository.saveAll(itemsToUpdate)
    }

    private fun getNewQueueItemOrder(queue: List<WorkQueueItem>, expectedItemOrder: Int): Int {
        val maxQueueOrder = queue.maxOfOrNull { it.order } ?: 0

        return if (maxQueueOrder < expectedItemOrder - 1) maxQueueOrder + 1
        else expectedItemOrder
    }
}