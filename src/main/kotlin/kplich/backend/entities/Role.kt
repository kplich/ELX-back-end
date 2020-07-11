package kplich.backend.entities

import javax.persistence.*

@Entity
@Table(name = "roles")
data class Role (
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Int,
        @Enumerated(EnumType.STRING) var name: RoleEnum
) {
    enum class RoleEnum {
        ROLE_ADMIN, ROLE_USER
    }
}
