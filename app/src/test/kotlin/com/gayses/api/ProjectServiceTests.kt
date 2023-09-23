package com.gayses.api

import com.gayses.api.data.model.Account
import com.gayses.api.data.model.Project
import com.gayses.api.data.repository.ProjectRepository
import com.gayses.api.exception.InvalidServiceArguments
import com.gayses.api.exception.ResourceAccessDeniedException
import com.gayses.api.exception.ResourceNotFoundException
import com.gayses.api.service.project.ProjectService
import com.gayses.api.service.project.ProjectServiceImpl
import com.gayses.api.store.MockAccountRepository
import com.gayses.api.store.MockProjectRepository
import io.mockk.coVerify
import io.mockk.junit5.MockKExtension
import io.mockk.slot
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class ProjectServiceTests {
    private val projectRepository: ProjectRepository = MockProjectRepository.create()

    private val projectService: ProjectService = ProjectServiceImpl(
        projectRepository
    )

    @Test
    fun createProject_whenGivesValidData_thenReturnsCreatedProject() {
        val owner = MockAccountRepository.data.first()
        val title = "Test project"

        val expectedOwner = MockAccountRepository.data.first()
        val expectedTitle = "Test project"

        val captor = slot<Project>()

        val result = projectService.createProject(owner, title)

        coVerify {
            projectRepository.save(capture(captor))
        }

        Assertions.assertEquals(expectedTitle, captor.captured.title)
        Assertions.assertEquals(expectedTitle, result.title)
        Assertions.assertEquals(expectedOwner, result.owner)
    }

    @Test
    fun createProject_whenGivesTitleWithLeadingAndTrailingWhitespaces_thenReturnsCreatedProjectWithTrimmedTitle() {
        val owner = MockAccountRepository.data.first()
        val title = "  Test project  "

        val expectedOwner = MockAccountRepository.data.first()
        val expectedTitle = "Test project"

        val captor = slot<Project>()

        val result = projectService.createProject(owner, title)

        coVerify {
            projectRepository.save(capture(captor))
        }

        Assertions.assertEquals(expectedTitle, captor.captured.title)
        Assertions.assertEquals(expectedTitle, result.title)
        Assertions.assertEquals(expectedOwner, result.owner)
    }

    @Test
    fun createProject_whenGivesBlankTitle_thenThrowsException() {
        val owner = MockAccountRepository.data.first()
        val title = "   "

        Assertions.assertThrows(InvalidServiceArguments::class.java) {
            projectService.createProject(owner, title)
        }
    }

    @Test
    fun getProject_whenGivesExistedProjectIdAndOwnerAccount_thenReturnsProjectWithSuchId() {
        val expectedProject = MockProjectRepository.data.first()

        val projectId = expectedProject.id
        val owner = expectedProject.owner

        val result = projectService.getProject(projectId, owner)

        Assertions.assertEquals(expectedProject, result)
    }

    @Test
    fun getProject_whenGivesNotExistedProjectIdAndOwnerAccount_thenThrowsException() {
        val project = MockProjectRepository.data.first()

        val projectId = 9999L
        val owner = project.owner

        Assertions.assertThrows(ResourceNotFoundException::class.java) {
            projectService.getProject(projectId, owner)
        }
    }

    @Test
    fun getProject_whenGivesExistedProjectIdAndNotOwnerAccount_thenThrowsException() {
        val project = MockProjectRepository.data.first()

        val projectId = project.id
        val owner = MockAccountRepository.data.first {
            project.owner != it
        }

        Assertions.assertThrows(ResourceAccessDeniedException::class.java) {
            projectService.getProject(projectId, owner)
        }
    }

    @Test
    fun getProject_whenGivesNotExistedProjectIdAndNotOwnerAccount_thenThrowsException() {
        val project = MockProjectRepository.data.first()

        val projectId = 9999L
        val owner = MockAccountRepository.data.first {
            project.owner != it
        }

        Assertions.assertThrows(ResourceNotFoundException::class.java) {
            projectService.getProject(projectId, owner)
        }
    }

    @Test
    fun getAllProjects_whenGivesAccount_thenReturnsProjectAssociatedWithAccount() {
        val account = MockAccountRepository.data.first()

        val expected = MockProjectRepository.data.filter {
            it.owner == account
        }

        val result = projectService.getAllProjects(account)

        Assertions.assertIterableEquals(expected, result)
    }

    @Test
    fun getAllProjects_whenGivesAccountWithNoProjects_thenReturnsEmptyList() {
        val account = Account(999L, "", "")

        val expectedSize = 0

        val result = projectService.getAllProjects(account)

        Assertions.assertEquals(expectedSize, result.size)
    }

    @Test
    fun updateProject_whenGivesValidData_thenReturnsCreatedProject() {
        val projectId = MockProjectRepository.data.first().id
        val owner = MockAccountRepository.data.first()
        val title = "Test project"

        val expectedOwner = MockAccountRepository.data.first()
        val expectedTitle = "Test project"

        val captor = slot<Project>()

        val result = projectService.updateProject(projectId, owner, title)

        coVerify {
            projectRepository.save(capture(captor))
        }

        Assertions.assertEquals(expectedTitle, captor.captured.title)
        Assertions.assertEquals(expectedTitle, result.title)
        Assertions.assertEquals(expectedOwner, result.owner)
    }

    @Test
    fun updateProject_whenGivesTitleWithLeadingAndTrailingWhitespaces_thenReturnsCreatedProjectWithTrimTitle() {
        val projectId = MockProjectRepository.data.first().id
        val owner = MockAccountRepository.data.first()
        val title = "  Test project  "

        val expectedOwner = MockAccountRepository.data.first()
        val expectedTitle = "Test project"

        val captor = slot<Project>()

        val result = projectService.updateProject(projectId, owner, title)

        coVerify {
            projectRepository.save(capture(captor))
        }

        Assertions.assertEquals(expectedTitle, captor.captured.title)
        Assertions.assertEquals(expectedTitle, result.title)
        Assertions.assertEquals(expectedOwner, result.owner)
    }

    @Test
    fun updateProject_whenGivesBlankTitle_thenThrowsException() {
        val projectId = MockProjectRepository.data.first().id
        val owner = MockAccountRepository.data.first()
        val title = "   "

        Assertions.assertThrows(InvalidServiceArguments::class.java) {
            projectService.updateProject(projectId, owner, title)
        }
    }

    @Test
    fun updateProject_whenGivesNotExistedProjectIdAndOwnerAccount_thenThrowsException() {
        val project = MockProjectRepository.data.first()

        val projectId = 999L
        val owner = project.owner

        val title = "Test project"

        Assertions.assertThrows(ResourceNotFoundException::class.java) {
            projectService.updateProject(projectId, owner, title)
        }
    }

    @Test
    fun updateProject_whenGivesExistedProjectIdAndNotOwnerAccount_thenThrowsException() {
        val project = MockProjectRepository.data.first()

        val projectId = project.id
        val owner = MockAccountRepository.data.first {
            project.owner != it
        }

        val title = "Test project"

        Assertions.assertThrows(ResourceAccessDeniedException::class.java) {
            projectService.updateProject(projectId, owner, title)
        }
    }

    @Test
    fun updateProject_whenGivesNotExistedProjectIdAndNotOwnerAccount_thenThrowsException() {
        val project = MockProjectRepository.data.first()

        val projectId = 999L
        val owner = MockAccountRepository.data.first {
            project.owner != it
        }

        val title = "Test project"

        Assertions.assertThrows(ResourceNotFoundException::class.java) {
            projectService.updateProject(projectId, owner, title)
        }
    }

    @Test
    fun deleteProject_whenGivesExistedProjectIdAndNotOwnerAccount_thenProjectDeletes() {
        val project = MockProjectRepository.data.first()

        val projectId = project.id
        val owner = project.owner

        val captor = slot<Project>()

        projectService.deleteProject(projectId, owner)

        coVerify {
            projectRepository.delete(capture(captor))
        }

        Assertions.assertEquals(projectId, captor.captured.id)
    }

    @Test
    fun deleteProject_whenGivesNotExistedProjectIdAndOwnerAccount_thenThrowsException() {
        val project = MockProjectRepository.data.first()

        val projectId = 999L
        val owner = project.owner

        Assertions.assertThrows(ResourceNotFoundException::class.java) {
            projectService.deleteProject(projectId, owner)
        }
    }

    @Test
    fun deleteProject_whenGivesExistedProjectIdAndNotOwnerAccount_thenThrowsException() {
        val project = MockProjectRepository.data.first()

        val projectId = project.id
        val owner = MockAccountRepository.data.first {
            it != project.owner
        }

        Assertions.assertThrows(ResourceAccessDeniedException::class.java) {
            projectService.deleteProject(projectId, owner)
        }
    }

    @Test
    fun deleteProject_whenGivesNotExistedProjectIdAndNotOwnerAccount_thenThrowsException() {
        val project = MockProjectRepository.data.first()

        val projectId = 999L
        val owner = MockAccountRepository.data.first {
            it != project.owner
        }

        Assertions.assertThrows(ResourceNotFoundException::class.java) {
            projectService.deleteProject(projectId, owner)
        }
    }
}
