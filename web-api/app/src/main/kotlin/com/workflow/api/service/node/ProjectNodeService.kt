package com.workflow.api.service.node

import com.workflow.api.data.model.Project
import com.workflow.api.data.model.ProjectNodeItem
import com.workflow.api.exception.InvalidServiceArguments
import com.workflow.api.exception.ResourceNotFoundException
import java.util.*

interface ProjectNodeService {
    @Throws(InvalidServiceArguments::class)
    fun createNode(
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
    ): ProjectNodeItem

    @Throws(ResourceNotFoundException::class)
    fun getNode(project: Project, nodeId: Long): ProjectNodeItem

    fun getAllNodes(project: Project): List<ProjectNodeItem>

    @Throws(InvalidServiceArguments::class)
    fun updateNode(
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
    ): ProjectNodeItem

    fun deleteNode(project: Project, nodeId: Long)
}