package com.gayses.api.data.model

import com.gayses.api.data.model.base.BaseModel
import jakarta.persistence.*

@Entity
@Table(name = "project")
class Project(
    id: Long?,

    @Column(name = "Title", nullable = false)
    var title: String,

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "OwnerId", nullable = false)
    var owner: Account,

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    val queue: Set<WorkQueueItem> = setOf()
) : BaseModel<Long>(id)
