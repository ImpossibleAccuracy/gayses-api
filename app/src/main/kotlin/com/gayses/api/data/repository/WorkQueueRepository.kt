package com.gayses.api.data.repository

import com.gayses.api.data.model.WorkQueueItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface WorkQueueRepository : JpaRepository<WorkQueueItem, Long> {
    fun findByProject_IdOrderByOrderAsc(projectId: Long): List<WorkQueueItem>

    fun findByWork__idAndProject_Id(workId: Long, projectId: Long): Optional<WorkQueueItem>

    fun existsByWork__idAndProject_Id(workId: Long, projectId: Long): Boolean
}