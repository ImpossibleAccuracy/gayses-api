package com.gayses.tests.data

import com.gayses.api.data.model.*
import com.gayses.api.data.model.Unit
import java.util.concurrent.atomic.AtomicLong

object TestsDataStore {
    val idGenerator = AtomicLong(100L)

    val accounts: List<Account>
        get() = listOf(
            Account(1, "admin@email.com", TestsUtils.encryptPassword("123")),
            Account(2, "user@email.com", TestsUtils.encryptPassword("456"))
        )

    val projects: List<Project>
        get() = listOf(
            Project(1, "Title#1", accounts[0]),
            Project(2, "Title#2", accounts[1]),
            Project(3, "Title#3", accounts[1]),
        )

    val units: List<Unit>
        get() = listOf(
            Unit(1, "mm"),
            Unit(2, "cm"),
        )

    val workTypes: List<WorkType>
        get() = listOf(
            WorkType(1, "Type#1"),
            WorkType(2, "Type#2"),
        )

    val performers: List<Performer>
        get() = listOf(
            Performer(1, "Performer#1"),
            Performer(2, "Performer#2"),
        )

    val works: List<Work>
        get() = listOf(
            Work(1, workTypes.first(), "Title#1", "ProductTitle#1", units.first(), 10, performers.first()),
            Work(2, workTypes[1], "Title#2", "ProductTitle#2", units[1], 2, performers[1]),
            Work(3, workTypes.first(), "Title#3", "ProductTitle#3", null, 6, performers.first()),
            Work(4, workTypes[1], "Title#4", "ProductTitle#4", units.first(), 1, performers[1]),
            Work(10, workTypes.first(), "Title#10", "ProductTitle#10", null, 23, performers.first()),
            Work(11, workTypes[1], "Title#11", "ProductTitle#11", units[1], 67, performers[1]),
            Work(12, workTypes.first(), "Title#12", "ProductTitle#12", units[1], 1, performers.first()),
            Work(13, workTypes[1], "Title#13", "ProductTitle#13", units.first(), 7, performers[1]),
        )

    val workQueueItems: List<WorkQueueItem>
        get() = listOf(
            WorkQueueItem(1, works[0], 0, projects[0]),
            WorkQueueItem(2, works[1], 0, projects[1]),
            WorkQueueItem(3, works[2], 1, projects[0]),
            WorkQueueItem(45, works[3], 1, projects[1]),
            WorkQueueItem(6767, works[4], 2, projects[0]),
            WorkQueueItem(12345, works[5], 2, projects[1]),
            WorkQueueItem(12357, works[6], 3, projects[0]),
            WorkQueueItem(123643, works[7], 3, projects[1]),
        )
}