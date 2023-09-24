package com.gayses.api.service.work

import com.gayses.api.data.model.Project
import com.gayses.api.data.model.Work
import com.gayses.api.data.model.WorkQueueItem
import com.gayses.api.exception.InvalidServiceArguments
import com.gayses.api.exception.ResourceNotFoundException
import java.util.*

interface WorkService {
    @Throws(InvalidServiceArguments::class)
    fun createWork(
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
    ): WorkQueueItem

    @Throws(ResourceNotFoundException::class)
    fun getWork(project: Project, workId: Long): Work

    fun getWorkQueue(project: Project): List<WorkQueueItem>

    @Throws(InvalidServiceArguments::class)
    fun updateWork(
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
    ): Work

    @Throws(InvalidServiceArguments::class)
    fun reorderWorkOrder(project: Project, work: Work, newOrder: Int): WorkQueueItem

    fun deleteWork(project: Project, workId: Long)
}