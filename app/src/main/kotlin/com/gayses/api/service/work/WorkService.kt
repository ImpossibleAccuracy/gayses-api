package com.gayses.api.service.work

import com.gayses.api.data.model.Project
import com.gayses.api.data.model.Work
import com.gayses.api.data.model.WorkQueueItem
import java.util.*

interface WorkService {
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

    fun getWork(project: Project, workId: Long): Work

    fun getWorkQueue(project: Project): List<WorkQueueItem>

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

    fun reorderWorkOrder(project: Project, work: Work, newOrder: Int): WorkQueueItem

    fun deleteWork(project: Project, workId: Long)
}