package com.gayses.api.service.project

import com.gayses.api.data.model.Account
import com.gayses.api.data.model.Project
import com.gayses.api.data.repository.ProjectRepository
import com.gayses.api.exception.ResourceAccessDeniedException
import com.gayses.api.exception.ResourceNotFoundException
import org.springframework.stereotype.Service

@Service
class ProjectServiceImpl(
    private val projectRepository: ProjectRepository
) : ProjectService {
    override fun createProject(title: String, owner: Account): Project =
        Project(null, title.trim(), owner).let {
            projectRepository.save(it)
        }

    override fun getProject(projectId: Long, account: Account) =
        projectRepository
            .findById(projectId)
            .orElseThrow {
                ResourceNotFoundException("Project($projectId) not found")
            }
            .also {
                if (it.owner.id != account.id) {
                    // TODO: impl viewers list
                    throw ResourceAccessDeniedException("You haven't access to this project")
                }
            }
}