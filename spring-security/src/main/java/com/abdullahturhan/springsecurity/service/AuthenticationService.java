package com.abdullahturhan.springsecurity.service;

import javax.management.relation.Role;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.abdullahturhan.springsecurity.dto.UserDto;
import com.abdullahturhan.springsecurity.dto.UserRequest;
import com.abdullahturhan.springsecurity.dto.UserResponse;
import com.abdullahturhan.springsecurity.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    
    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public UserResponse save(UserDto userDto) {
        User user = User.builder().username(userDto.getUsername())
        .password(userDto.getPassword())
        .fullName(userDto.getFullName())
        .email(userDto.getEmail())
        .role(Role.USER).build();

        userRepository.save(user);
        var token = jwtService.generateToken();
        return UserResponse.builder().token(token).build();
    }

    public UserResponse auth(UserRequest userRequest){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRequest.getUsername(), userRequest.getPassword()));
        User user = userRepository.getUserByUsername(userRequest.getUsername().orElseThrow());
        String token = jwtService.generateToken(user);
        return UserResponse.builder().token(token).build();
    }
    
}
 