package com.banquemisr.challenge05.service.user;

import com.banquemisr.challenge05.dto.user.request.LoginRequestDTO;
import com.banquemisr.challenge05.dto.user.request.SignUpRequestDTO;
import com.banquemisr.challenge05.dto.user.response.LoginResponseDTO;

public interface IUserService {
    void register(SignUpRequestDTO signUpRequestDTO);

    LoginResponseDTO authenticate(LoginRequestDTO loginRequestDTO);
}
