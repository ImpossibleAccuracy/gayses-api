package com.gayses.api.data.repository

import com.gayses.api.data.model.WorkStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WorkStatusRepository : JpaRepository<WorkStatus, Long>