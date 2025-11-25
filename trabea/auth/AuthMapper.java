package com.bc46.trabea.auth;

import com.bc46.trabea.auth.dto.AuthPartTimeRegisterRequest;
import com.bc46.trabea.auth.dto.AuthPartTimeRegisterResponse;
import com.bc46.trabea.parttimeemployee.PartTimeEmployee;
import com.bc46.trabea.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "joinDate", ignore = true)
    @Mapping(target = "resignDate", ignore = true)
    PartTimeEmployee toPartTimeEmployee(AuthPartTimeRegisterRequest request);

    @Mapping(target = "firstName", source = "partTimeEmployee.firstName")
    @Mapping(target = "lastName", source = "partTimeEmployee.lastName")
    AuthPartTimeRegisterResponse toPartTimeRegisterResponse(User user, PartTimeEmployee partTimeEmployee);
}
