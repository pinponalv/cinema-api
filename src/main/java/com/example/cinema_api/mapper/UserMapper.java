package com.example.cinema_api.mapper;

import com.example.cinema_api.dto.PermissionResponse;
import com.example.cinema_api.dto.RoleResponse;
import com.example.cinema_api.dto.UserResponse;
import com.example.cinema_api.entity.Permission;
import com.example.cinema_api.entity.Roles;
import com.example.cinema_api.entity.UserSec;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserDTO(UserSec userSec);

    /** Mapea entidad Roles → DTO RoleResponse. MapStruct necesita ayuda para mapear
     *  campos con nombres diferentes: la entidad usa "permissionsList" pero el DTO
     *  espera "permissions". Sin este @Mapping, el campo quedaría null. */
    @Mapping(target = "permissions", source = "permissionsList")
    RoleResponse toRoleDTO(Roles roles);

    /** Mapea entidad Permission → DTO PermissionResponse. Aqui también los nombres
     *  difieren: la entidad tiene "permissionName" pero el DTO usa "permission".
     *  El @Mapping le dice a MapStruct que debe copiar permissionName al campo
     *  "permission" del DTO, evitando que quede null. */
    @Mapping(target = "permission", source = "permissionName")
    PermissionResponse toPermissionDTO(Permission permission);
}
