package kplich.backend.entities

import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Entity
@Table(name = "users")
data class ApplicationUser(
        @NotBlank
        @Size(min = 3, max = 20)
        var username: String,

        @NotBlank
        @Size(min = 8, max = 40)
        var password: String,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = -1,

        var joined: LocalDateTime = LocalDateTime.now(),

        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(
                name = "user_roles",
                joinColumns = [JoinColumn(name = "id")],
                inverseJoinColumns = [JoinColumn(name = "role_id")])
        var roles: MutableSet<Role> = hashSetOf()
)
