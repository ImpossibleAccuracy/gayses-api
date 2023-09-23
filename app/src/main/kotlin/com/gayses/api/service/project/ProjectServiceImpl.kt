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

    override fun getProject(projectId: Long, account: Account): Project =
        getProjectOrThrow(projectId)
            .also {
                if (it.owner.id != account.id) {
                    // TODO: impl viewers list
                    throw ResourceAccessDeniedException("You haven't access to this project")
                }
            }

    override fun getAllProjects(account: Account): List<Project> =
        projectRepository.findByOwner_IdOrderByTitleAsc(account.id)

    override fun updateProject(projectId: Long, account: Account, title: String): Project =
        getProjectOrThrow(projectId)
            .also {
                if (it.owner.id != account.id) {
                    // TODO: impl editors list
                    throw ResourceAccessDeniedException("You haven't access to this project")
                }
            }
            .let {
                it.title = title

                projectRepository.save(it)
            }

    override fun deleteProject(projectId: Long, account: Account) =
        getProjectOrThrow(projectId)
            .also {
                if (it.owner.id != account.id) {
                    // TODO: impl owners list
                    throw ResourceAccessDeniedException("You haven't access to this project")
                }
            }
            .let {
                projectRepository.delete(it)
            }

    private fun getProjectOrThrow(id: Long): Project =
        projectRepository
            .findById(id)
            .orElseThrow {
                ResourceNotFoundException("Project($id) not found")
            }
}