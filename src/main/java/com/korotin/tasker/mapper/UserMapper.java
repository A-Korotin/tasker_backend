package com.korotin.tasker.mapper;

import com.korotin.tasker.domain.User;
import com.korotin.tasker.domain.dto.OutputUserDTO;
import com.korotin.tasker.domain.dto.RegisterUserDto;
import com.korotin.tasker.domain.dto.UserDTO;
import com.korotin.tasker.mapper.config.MapConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapConfig.class)
public interface UserMapper {

    @Mapping(target = "role", constant = "USER")
    User registerDTOToUser(RegisterUserDto dto);

    OutputUserDTO userToDTO(User user);

    User DTOToUser(UserDTO userDTO);
}
