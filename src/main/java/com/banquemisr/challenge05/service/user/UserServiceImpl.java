package com.banquemisr.challenge05.service.user;

import com.banquemisr.challenge05.exception.UserLoginException;
import com.banquemisr.challenge05.exception.UserRegistrationException;
import com.banquemisr.challenge05.repository.UserRepository;
import com.banquemisr.challenge05.mapper.UserMapper;
import com.banquemisr.challenge05.model.User;
import com.banquemisr.challenge05.dto.user.request.LoginRequestDTO;
import com.banquemisr.challenge05.dto.user.request.SignUpRequestDTO;
import com.banquemisr.challenge05.dto.user.response.LoginResponseDTO;
import com.banquemisr.challenge05.security.JwtUtils;
import com.banquemisr.challenge05.service.email.IEmailService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtService;
    private final IEmailService iEmailService;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtils jwtService, IEmailService iEmailService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.iEmailService = iEmailService;
    }

    @Override
    public void register(SignUpRequestDTO signUpRequestDTO) {
        log.info("register  with details: {}", signUpRequestDTO);

        validateEmail(signUpRequestDTO.getEmail());

        try {
            User user = userMapper.mapRegisterRequestToUser(signUpRequestDTO);
            user.setPassword(passwordEncoder.encode(signUpRequestDTO.getPassword()));
            userRepository.save(user);
            log.info("User registered successfully with this email: {}", signUpRequestDTO.getEmail());
            String subject = "Welcome to the Task Management APP!";
            String body = String.format(
                    "Dear %s,\n\n" +
                            "Congratulations on successfully signing up to our system.\n\n" +
                            "Best Regards,\n",
                    user.getUsername()
            );
            iEmailService.sendEmail(user.getEmail(), subject, body);

            log.info("Welcome email sent to: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Error during user registration: {}", e.getMessage(), e);
            throw new UserRegistrationException("Error while trying to register user with details: " + signUpRequestDTO, e);
        }
    }

    @Override
    public LoginResponseDTO authenticate(LoginRequestDTO loginRequestDTO) {
        log.info("Trying to authenticate user with email: {}", loginRequestDTO.getEmail());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword())
            );

            User authenticatedUser = userRepository.findByEmail(loginRequestDTO.getEmail())
                    .orElseThrow(() -> new NotFoundException("Invalid username or password"));

            String jwtToken = jwtService.generateToken(authenticatedUser);
            log.info("Authentication successful for user with email: {}", loginRequestDTO.getEmail());

            return new LoginResponseDTO(jwtToken);

        } catch (NotFoundException ex) {
            log.error("Authentication failed for user with email: {}", loginRequestDTO.getEmail());
            throw new UserLoginException("Authentication failed: Invalid credentials for " + loginRequestDTO.getEmail());
        } catch (Exception e) {
            log.error("Authentication error: {}", e.getMessage(), e);
            throw new UserLoginException("Authentication failed for user with email: " + loginRequestDTO.getEmail(), e);
        }
    }

    private void validateEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            String errorMessage = "Email already exists, please insert a valid email.";
            log.error(errorMessage);
            throw new UserRegistrationException(errorMessage);
        }
    }
}
