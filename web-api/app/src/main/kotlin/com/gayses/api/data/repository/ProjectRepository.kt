package com.gayses.api.data.repository

import com.gayses.api.data.model.Project
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProjectRepository : JpaRepository<Project, Long> {
    fun findByOwner_IdOrderByCreatedAtAscTitleAsc(id: Long): List<Project>
}