package com.workflow.api.service.project

import com.workflow.api.data.model.Account
import com.workflow.api.data.model.Project
import com.workflow.api.exception.InvalidServiceArguments
import com.workflow.api.exception.OperationDeniedException
import com.workflow.api.exception.ResourceAccessDeniedException

interface ProjectService {
    @Throws(InvalidServiceArguments::class, OperationDeniedException::class)
    fun createProject(owner: Account, title: String): Project

    @Throws(ResourceAccessDeniedException::class, ResourceAccessDeniedException::class)
    fun getProject(projectId: Long, account: Account): Project

    fun getAllProjects(account: Account): List<Project>

    @Throws(InvalidServiceArguments::class, ResourceAccessDeniedException::class, OperationDeniedException::class)
    fun updateProject(projectId: Long, account: Account, title: String): Project

    @Throws(ResourceAccessDeniedException::class, OperationDeniedException::class)
    fun deleteProject(projectId: Long, account: Account)
}