package com.hb.cda.springholiday.controller.dto.mapper;

import com.hb.cda.springholiday.controller.dto.UserDTO;
import com.hb.cda.springholiday.controller.dto.UserRegisterDTO;
import com.hb.cda.springholiday.entity.User;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    User convertToEntity(UserRegisterDTO dto);
    UserDTO convertToDTO(User user);
}
