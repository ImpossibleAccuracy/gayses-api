package com.gayses.api.service.work

import com.gayses.api.data.model.Project
import com.gayses.api.data.model.Work
import java.util.*

interface WorkService {
    fun createWork(
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
    ): Work
}