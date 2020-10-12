package kplich.backend.authentication.entities

import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Entity
@Table(name = "users", indexes = [Index(columnList = "username", unique = true)])
data class ApplicationUser(
        @NotBlank
        @Size(min = 3, max = 20)
        var username: String,

        @NotBlank
        @Size(min = 8, max = 40)
        var password: String,

        @Size(min = ETHEREUM_ADDRESS_LENGTH, max = ETHEREUM_ADDRESS_LENGTH)
        // TODO: address validation through regex (or even checksum)
        var ethereumAddress: String? = null,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = -1,

        var joined: LocalDateTime = LocalDateTime.now(),

        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(
                name = "user_roles",
                joinColumns = [JoinColumn(name = "user_id")],
                inverseJoinColumns = [JoinColumn(name = "role_id")])
        var roles: MutableSet<Role> = hashSetOf()
) {
    companion object {
        const val ETHEREUM_ADDRESS_LENGTH = 42
    }
}
