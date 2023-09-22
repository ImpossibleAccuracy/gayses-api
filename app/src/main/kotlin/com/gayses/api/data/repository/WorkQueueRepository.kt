package com.gayses.api.data.repository;

import com.gayses.api.data.model.WorkQueueItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WorkQueueRepository : JpaRepository<WorkQueueItem, Long> {
    fun findByProject_IdOrderByOrderAsc(id: Long): List<WorkQueueItem>
}