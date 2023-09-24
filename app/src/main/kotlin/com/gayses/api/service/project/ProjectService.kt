package com.gayses.api.service.project

import com.gayses.api.data.model.Account
import com.gayses.api.data.model.Project
import com.gayses.api.exception.InvalidServiceArguments
import com.gayses.api.exception.OperationDeniedException
import com.gayses.api.exception.ResourceAccessDeniedException

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