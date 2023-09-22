package com.gayses.api.data.repository;

import com.gayses.api.data.model.Work
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WorkRepository : JpaRepository<Work, Long>