package kplich.backend.authentication.entities

import javax.persistence.*

@Entity
@Table(name = "roles")
data class Role (
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Int,
        @Enumerated(EnumType.STRING) var name: RoleEnum
) {
    enum class RoleEnum {
        ROLE_USER
    }
}
