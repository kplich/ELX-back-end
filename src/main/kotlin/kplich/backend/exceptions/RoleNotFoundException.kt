package kplich.backend.exceptions

import kplich.backend.entities.Role

class RoleNotFoundException(role: Role.RoleEnum): ElxException("Role $role not found!")
