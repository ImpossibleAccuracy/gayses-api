package com.gayses.api.data.repository

import com.gayses.api.data.model.ProjectNodeItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProjectNodeItemRepository : JpaRepository<ProjectNodeItem, Long> {
    fun findByIdAndProject_Id(id: Long, projectId: Long): Optional<ProjectNodeItem>


    fun findByProject_IdOrderByCreatedAtAscParentNode_CreatedAtAsc(id: Long): List<ProjectNodeItem>
}