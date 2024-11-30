package com.banquemisr.challenge05.mapper;

import com.banquemisr.challenge05.dto.user.request.SignUpRequestDTO;
import com.banquemisr.challenge05.model.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-30T12:59:54+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User mapRegisterRequestToUser(SignUpRequestDTO signUpRequestDTO) {
        if ( signUpRequestDTO == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.userName( signUpRequestDTO.getUserName() );
        user.email( signUpRequestDTO.getEmail() );
        user.password( signUpRequestDTO.getPassword() );
        user.role( signUpRequestDTO.getRole() );

        return user.build();
    }
}
