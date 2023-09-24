package com.gayses.tests.store

import com.gayses.api.data.model.Project
import com.gayses.api.data.repository.ProjectRepository
import com.gayses.tests.data.TestsDataStore
import io.mockk.every
import io.mockk.mockk
import java.util.*

object MockProjectRepository : MockStore<Project>() {
    override fun create() =
        mockk<ProjectRepository> {
            // FIND BY ID
            every { findById(any()) }
                .returns(Optional.empty())

            TestsDataStore.projects.forEach {
                every { findById(it.id) }
                    .returns(Optional.of(it))
            }

            // FIND BY OWNER ID
            every { findByOwner_IdOrderByTitleAsc(any()) }
                .returns(listOf())

            TestsDataStore.accounts.forEach { account ->
                every { findByOwner_IdOrderByTitleAsc(account.id) }
                    .returns(TestsDataStore.projects.filter {
                        it.owner == account
                    })
            }

            // SAVE
            every { save(any()) }
                .answers {
                    val item = firstArg() as Project

                    item.updateId(idGenerator.incrementAndGet())

                    item
                }

            // DELETE
            every { delete(any()) }
                .returns(Unit)
        }
}