package com.workflow.api.data.model

import com.workflow.api.data.model.base.BaseModel
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.Instant

@Entity
@Table(name = "account")
class Account(
    id: Long?,

    @Column(name = "Email", nullable = false)
    var email: String,

    @Column(name = "Password", nullable = false)
    var passwordHash: String,

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    val projects: Set<Project> = setOf(),

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "Account_Role",
        joinColumns = [JoinColumn(name = "AccountId")],
        inverseJoinColumns = [JoinColumn(name = "RoleId")]
    )
    val roles: Set<AccountRole> = setOf()
) : BaseModel<Long>(id), UserDetails {
    override fun getAuthorities(): Set<GrantedAuthority> =
        roles
            .map {
                SimpleGrantedAuthority("ROLE_${it.title}")
            }
            .toSet()

    override fun getPassword(): String =
        passwordHash

    override fun getUsername(): String =
        email

    override fun isAccountNonExpired(): Boolean =
        true

    override fun isAccountNonLocked(): Boolean =
        true

    override fun isCredentialsNonExpired(): Boolean =
        true

    override fun isEnabled(): Boolean =
        true

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Account) return false
        if (!super.equals(other)) return false

        if (email != other.email) return false
        if (passwordHash != other.passwordHash) return false
        if (projects != other.projects) return false
        if (roles != other.roles) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + passwordHash.hashCode()
        result = 31 * result + projects.hashCode()
        result = 31 * result + roles.hashCode()
        return result
    }

    override fun toString(): String {
        return "Account(id=$id, email='$email')"
    }
}
