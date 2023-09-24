package com.gayses.api.data.repository

import com.gayses.api.data.model.Work
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface WorkRepository : JpaRepository<Work, Long> {
    fun findBy_idAndQueueItem_Project_Id(id: Long, projectId: Long): Optional<Work>
}