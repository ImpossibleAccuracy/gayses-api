package com.gayses.api.service.project

import com.gayses.api.data.model.Account
import com.gayses.api.data.model.Project
import com.gayses.api.exception.OperationRejectedException
import com.gayses.api.exception.ResourceAccessDeniedException

interface ProjectService {
    @Throws(OperationRejectedException::class)
    fun createProject(title: String, owner: Account): Project

    @Throws(ResourceAccessDeniedException::class)
    fun getProject(projectId: Long, account: Account): Project

    fun getAllProjects(account: Account): List<Project>

    @Throws(ResourceAccessDeniedException::class, OperationRejectedException::class)
    fun updateProject(projectId: Long, account: Account, title: String): Project

    @Throws(ResourceAccessDeniedException::class, OperationRejectedException::class)
    fun deleteProject(projectId: Long, account: Account)
}