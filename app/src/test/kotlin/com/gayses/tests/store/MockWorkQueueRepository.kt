package com.gayses.tests.store

import com.gayses.api.data.model.WorkQueueItem
import com.gayses.api.data.repository.WorkQueueRepository
import com.gayses.tests.data.TestsDataStore
import io.mockk.every
import io.mockk.mockk
import java.util.*


object MockWorkQueueRepository : MockStore<WorkQueueItem>() {
    override fun create() =
        mockk<WorkQueueRepository> {
            every { findByProject_IdOrderByOrderAsc(any()) }
                .answers {
                    val projectId = firstArg<Long>()

                    TestsDataStore.workQueueItems.filter {
                        it.project.id == projectId
                    }.sortedBy {
                        it.order
                    }
                }

            every { findByWork__idAndProject_Id(any(), any()) }
                .answers {
                    val workId = firstArg<Long>()
                    val projectId = secondArg<Long>()

                    TestsDataStore.workQueueItems.find {
                        it.work.id == workId && it.project.id == projectId
                    }.let {
                        Optional.ofNullable(it)
                    }
                }

            every { save(any()) }
                .answers {
                    val item = firstArg<WorkQueueItem>()

                    item.updateId(idGenerator.incrementAndGet())

                    item
                }

            every { saveAll(any<Iterable<WorkQueueItem>>()) }
                .answers {
                    val items = firstArg<Iterable<WorkQueueItem>>()

                    items.forEach {
                        it.updateId(idGenerator.incrementAndGet())
                    }

                    items.toList()
                }
        }
}