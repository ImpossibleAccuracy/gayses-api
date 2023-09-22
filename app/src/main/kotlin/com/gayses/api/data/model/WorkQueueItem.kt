package com.gayses.api.data.model

import com.gayses.api.data.model.base.BaseModel
import jakarta.persistence.*

@Entity
@Table(name = "workqueue")
class WorkQueueItem(
    id: Long?,

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "WorkId", nullable = false)
    var work: Work,

    @Column(name = "`Order`", nullable = false)
    var order: Int,

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ProjectId", nullable = false)
    var project: Project,
) : BaseModel<Long>(id)
