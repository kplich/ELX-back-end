package kplich.backend.services

import kplich.backend.entities.User
import kplich.backend.repositories.UserRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(
        private val userRepository: UserRepository
): UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username) ?: throw UsernameNotFoundException(username)

        return UserDetailsImpl(user)
    }

    inner class UserDetailsImpl(
            private val user: User,
            private val authorities: MutableCollection<out GrantedAuthority> = mutableListOf(SimpleGrantedAuthority("user"))
    ) : UserDetails {
        override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
            return authorities
        }

        override fun isEnabled(): Boolean {
            return true
        }

        override fun getUsername(): String {
            return user.username
        }

        override fun isCredentialsNonExpired(): Boolean {
            return true
        }

        override fun getPassword(): String {
            return user.password
        }

        override fun isAccountNonExpired(): Boolean {
            return true
        }

        override fun isAccountNonLocked(): Boolean {
            return true
        }
    }
}
