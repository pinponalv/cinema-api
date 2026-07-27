package com.example.cinema_api.mapper;

import com.example.cinema_api.dto.RoleResponse;
import com.example.cinema_api.dto.UserResponse;
import com.example.cinema_api.entity.Roles;
import com.example.cinema_api.entity.UserSec;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserDTO(UserSec userSec);
    RoleResponse toRoleDTO(Roles roles);
}
