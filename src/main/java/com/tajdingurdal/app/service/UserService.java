package com.tajdingurdal.app.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import com.tajdingurdal.app.shared.dto.UserDto;

public interface UserService extends UserDetailsService{

	UserDto createUser(UserDto user) throws Exception ;
	UserDto getUserByEmail(String email);
    UserDto getUserByUserId(String userId);
}
