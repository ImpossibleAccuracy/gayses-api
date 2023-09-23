package com.gayses.api.store

import com.gayses.api.data.model.Project
import com.gayses.api.data.repository.ProjectRepository
import io.mockk.coEvery
import io.mockk.mockk
import java.util.*

object MockProjectRepository : MockStore<Project>() {
    override val data: List<Project>
        get() = listOf(
            Project(1, "Title#1", MockAccountRepository.data[0]),
            Project(2, "Title#2", MockAccountRepository.data[1]),
            Project(3, "Title#3", MockAccountRepository.data[1]),
            Project(4, "Title#4", MockAccountRepository.data[0]),
        )

    override fun create() =
        mockk<ProjectRepository>().also { repo ->
            // FIND BY ID
            coEvery { repo.findById(any()) }
                .returns(Optional.empty())

            data.forEach {
                coEvery { repo.findById(it.id) }
                    .returns(Optional.of(it))
            }

            // FIND BY OWNER ID
            coEvery { repo.findByOwner_IdOrderByTitleAsc(any()) }
                .returns(listOf())

            MockAccountRepository.data.forEach { account ->
                coEvery { repo.findByOwner_IdOrderByTitleAsc(account.id) }
                    .returns(data.filter {
                        it.owner == account
                    })
            }

            // SAVE
            coEvery { repo.save(any()) }
                .answers {
                    val item = firstArg() as Project

                    item.updateId(idGenerator.incrementAndGet())

                    item
                }

            // DELETE
            coEvery { repo.delete(any()) }
                .returns(Unit)
        }
}