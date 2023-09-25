package com.workflow.api.data.repository

import com.workflow.api.data.model.ProjectNodeStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProjectNodeStatusRepository : JpaRepository<ProjectNodeStatus, Long>