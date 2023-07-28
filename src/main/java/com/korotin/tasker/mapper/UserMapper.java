package com.korotin.tasker.mapper;

import com.korotin.tasker.domain.User;
import com.korotin.tasker.domain.dto.RegisterUserDto;
import com.korotin.tasker.domain.dto.OutputUserDTO;
import com.korotin.tasker.domain.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", constant = "USER")
    @Mapping(target = "id", ignore = true)
    User registerDTOToUser(RegisterUserDto dto);

    OutputUserDTO userToDTO(User user);

    @Mapping(target = "id", ignore = true)
    User DTOToUser(UserDTO userDTO);
}
