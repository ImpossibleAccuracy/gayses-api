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
) : BaseModel<Long>(id) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Project) return false
        if (!super.equals(other)) return false

        if (title != other.title) return false
        if (owner != other.owner) return false
        if (queue != other.queue) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + owner.hashCode()
        result = 31 * result + queue.hashCode()
        return result
    }

    override fun toString(): String {
        return "Project(id=$id, title='$title', owner=$owner)"
    }
}
