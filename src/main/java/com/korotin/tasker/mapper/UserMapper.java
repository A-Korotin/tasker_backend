package com.korotin.tasker.mapper;

import com.korotin.tasker.domain.User;
import com.korotin.tasker.domain.dto.RegisterUserDto;
import com.korotin.tasker.domain.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "role", constant = "USER")
    User registerDTOToUser(RegisterUserDto dto);

    UserDTO userToDTO(User user);
}
