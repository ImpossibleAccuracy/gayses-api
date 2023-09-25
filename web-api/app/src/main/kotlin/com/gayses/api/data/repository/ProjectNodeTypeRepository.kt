package com.gayses.api.data.repository

import com.gayses.api.data.model.ProjectNodeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProjectNodeTypeRepository : JpaRepository<ProjectNodeType, Long> {
    fun findByTitleIgnoreCase(title: String): Optional<ProjectNodeType>
}