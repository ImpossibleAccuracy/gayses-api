package com.gayses.tests

import com.gayses.api.data.model.Account
import com.gayses.api.data.model.Project
import com.gayses.api.data.repository.ProjectRepository
import com.gayses.api.exception.InvalidServiceArguments
import com.gayses.api.exception.OperationDeniedException
import com.gayses.api.exception.ResourceAccessDeniedException
import com.gayses.api.exception.ResourceNotFoundException
import com.gayses.api.service.project.ProjectService
import com.gayses.api.service.project.ProjectServiceImpl
import com.gayses.tests.data.TestsDataStore
import com.gayses.tests.store.MockProjectRepository
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ProjectServiceTests {
    private val projectRepository: ProjectRepository = MockProjectRepository.create()

    private val projectService: ProjectService = ProjectServiceImpl(
        projectRepository
    )

    @Nested
    @DisplayName("Create project")
    inner class CreateProjectMethod {
        @Test
        fun whenGivesValidData_thenReturnsCreatedProject() {
            // arrange
            val owner = TestsDataStore.accounts.first()
            val title = "Test project"

            val expectedOwner = TestsDataStore.accounts.first()
            val expectedTitle = "Test project"

            // act
            val result = projectService.createProject(owner, title)

            // assert
            val captor = slot<Project>()
            verify { projectRepository.save(capture(captor)) }

            Assertions.assertEquals(expectedTitle, captor.captured.title)
            Assertions.assertEquals(expectedTitle, result.title)
            Assertions.assertEquals(expectedOwner, result.owner)
        }

        @Test
        fun whenGivesTitleWithLeadingAndTrailingWhitespaces_thenReturnsCreatedProjectWithTrimmedTitle() {
            // arrange
            val owner = TestsDataStore.accounts.first()
            val title = "  Test project  "

            val expectedOwner = TestsDataStore.accounts.first()
            val expectedTitle = "Test project"

            // act
            val result = projectService.createProject(owner, title)

            // assert
            val captor = slot<Project>()
            verify { projectRepository.save(capture(captor)) }

            Assertions.assertEquals(expectedTitle, captor.captured.title)
            Assertions.assertEquals(expectedTitle, result.title)
            Assertions.assertEquals(expectedOwner, result.owner)
        }

        @Test
        fun whenGivesBlankTitle_thenThrowsException() {
            // arrange
            val owner = TestsDataStore.accounts.first()
            val title = "   "

            // act
            Assertions.assertThrows(InvalidServiceArguments::class.java) {
                projectService.createProject(owner, title)
            }
        }
    }

    @Nested
    @DisplayName("Get project")
    inner class GetProjectMethod {
        @Test
        fun whenGivesExistedProjectIdAndOwnerAccount_thenReturnsProjectWithSuchId() {
            // arrange
            val expectedProject = TestsDataStore.projects.first()

            val projectId = expectedProject.id
            val owner = expectedProject.owner

            // act
            val result = projectService.getProject(projectId, owner)

            // assert
            Assertions.assertEquals(expectedProject, result)
        }

        @Test
        fun whenGivesNotExistedProjectIdAndOwnerAccount_thenThrowsException() {
            // arrange
            val project = TestsDataStore.projects.first()

            val projectId = 9999L
            val owner = project.owner

            // act
            Assertions.assertThrows(ResourceNotFoundException::class.java) {
                projectService.getProject(projectId, owner)
            }
        }

        @Test
        fun whenGivesExistedProjectIdAndNotOwnerAccount_thenThrowsException() {
            // arrange
            val project = TestsDataStore.projects.first()

            val projectId = project.id
            val owner = TestsDataStore.accounts.first {
                project.owner != it
            }

            // act
            Assertions.assertThrows(ResourceAccessDeniedException::class.java) {
                projectService.getProject(projectId, owner)
            }
        }

        @Test
        fun whenGivesNotExistedProjectIdAndNotOwnerAccount_thenThrowsException() {
            // arrange
            val project = TestsDataStore.projects.first()

            val projectId = 9999L
            val owner = TestsDataStore.accounts.first {
                project.owner != it
            }

            // act
            Assertions.assertThrows(ResourceNotFoundException::class.java) {
                projectService.getProject(projectId, owner)
            }
        }
    }

    @Nested
    @DisplayName("Get all project")
    inner class GetAllProjectsMethod {
        @Test
        fun whenGivesAccount_thenReturnsProjectAssociatedWithAccount() {
            // arrange
            val account = TestsDataStore.accounts.first()

            val expected = TestsDataStore.projects.filter {
                it.owner == account
            }

            // act
            val result = projectService.getAllProjects(account)

            // assert
            Assertions.assertIterableEquals(expected, result)
        }

        @Test
        fun whenGivesAccountWithNoProjects_thenReturnsEmptyList() {
            // arrange
            val account = Account(999L, "", "")

            val expectedSize = 0

            // act
            val result = projectService.getAllProjects(account)

            // assert
            Assertions.assertEquals(expectedSize, result.size)
        }
    }

    @Nested
    @DisplayName("Update project")
    inner class UpdateProjectMethod {
        @Test
        fun whenGivesValidData_thenReturnsCreatedProject() {
            // arrange
            val projectId = TestsDataStore.projects.first().id
            val owner = TestsDataStore.accounts.first()
            val title = "Test project"

            val expectedOwner = TestsDataStore.accounts.first()
            val expectedTitle = "Test project"

            // act
            val result = projectService.updateProject(projectId, owner, title)

            // assert
            val captor = slot<Project>()
            verify { projectRepository.save(capture(captor)) }

            Assertions.assertEquals(expectedTitle, captor.captured.title)
            Assertions.assertEquals(expectedTitle, result.title)
            Assertions.assertEquals(expectedOwner, result.owner)
        }

        @Test
        fun whenGivesTitleWithLeadingAndTrailingWhitespaces_thenReturnsCreatedProjectWithTrimTitle() {
            // arrange
            val projectId = TestsDataStore.projects.first().id
            val owner = TestsDataStore.accounts.first()
            val title = "  Test project  "

            val expectedOwner = TestsDataStore.accounts.first()
            val expectedTitle = "Test project"

            // act
            val result = projectService.updateProject(projectId, owner, title)

            // assert
            val captor = slot<Project>()
            verify { projectRepository.save(capture(captor)) }

            Assertions.assertEquals(expectedTitle, captor.captured.title)
            Assertions.assertEquals(expectedTitle, result.title)
            Assertions.assertEquals(expectedOwner, result.owner)
        }

        @Test
        fun whenGivesBlankTitle_thenThrowsException() {
            // arrange
            val projectId = TestsDataStore.projects.first().id
            val owner = TestsDataStore.accounts.first()
            val title = "   "

            // act
            Assertions.assertThrows(InvalidServiceArguments::class.java) {
                projectService.updateProject(projectId, owner, title)
            }
        }

        @Test
        fun whenGivesNotExistedProjectIdAndOwnerAccount_thenThrowsException() {
            // arrange
            val project = TestsDataStore.projects.first()

            val projectId = 999L
            val owner = project.owner

            val title = "Test project"

            // act
            Assertions.assertThrows(ResourceNotFoundException::class.java) {
                projectService.updateProject(projectId, owner, title)
            }
        }

        @Test
        fun whenGivesExistedProjectIdAndNotOwnerAccount_thenThrowsException() {
            // arrange
            val project = TestsDataStore.projects.first()

            val projectId = project.id
            val owner = TestsDataStore.accounts.first {
                project.owner != it
            }

            val title = "Test project"

            // act
            Assertions.assertThrows(OperationDeniedException::class.java) {
                projectService.updateProject(projectId, owner, title)
            }
        }

        @Test
        fun whenGivesNotExistedProjectIdAndNotOwnerAccount_thenThrowsException() {
            // arrange
            val project = TestsDataStore.projects.first()

            val projectId = 999L
            val owner = TestsDataStore.accounts.first {
                project.owner != it
            }

            val title = "Test project"

            // act
            Assertions.assertThrows(ResourceNotFoundException::class.java) {
                projectService.updateProject(projectId, owner, title)
            }
        }
    }

    @Nested
    @DisplayName("Delete project")
    inner class DeleteProjectMethod {
        @Test
        fun whenGivesExistedProjectIdAndNotOwnerAccount_thenProjectDeletes() {
            // arrange
            val project = TestsDataStore.projects.first()

            val projectId = project.id
            val owner = project.owner

            // act
            projectService.deleteProject(projectId, owner)

            // assert
            val captor = slot<Project>()
            verify { projectRepository.delete(capture(captor)) }

            Assertions.assertEquals(projectId, captor.captured.id)
        }

        @Test
        fun whenGivesNotExistedProjectIdAndOwnerAccount_thenThrowsException() {
            // arrange
            val project = TestsDataStore.projects.first()

            val projectId = 999L
            val owner = project.owner

            // act
            Assertions.assertThrows(ResourceNotFoundException::class.java) {
                projectService.deleteProject(projectId, owner)
            }
        }

        @Test
        fun whenGivesExistedProjectIdAndNotOwnerAccount_thenThrowsException() {
            // arrange
            val project = TestsDataStore.projects.first()

            val projectId = project.id
            val owner = TestsDataStore.accounts.first {
                it != project.owner
            }

            // act
            Assertions.assertThrows(OperationDeniedException::class.java) {
                projectService.deleteProject(projectId, owner)
            }
        }

        @Test
        fun whenGivesNotExistedProjectIdAndNotOwnerAccount_thenThrowsException() {
            // arrange
            val project = TestsDataStore.projects.first()

            val projectId = 999L
            val owner = TestsDataStore.accounts.first {
                it != project.owner
            }

            // act
            Assertions.assertThrows(ResourceNotFoundException::class.java) {
                projectService.deleteProject(projectId, owner)
            }
        }
    }
}