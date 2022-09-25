package com.tajdingurdal.app.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import com.tajdingurdal.app.shared.dto.UserDto;

import java.util.List;

public interface UserService extends UserDetailsService{

	UserDto createUser(UserDto user) throws Exception ;
	UserDto getUserByEmail(String email);
    UserDto getUserByUserId(String userId);
	UserDto updateUser(String id, UserDto userDto);
	void deleteUser(String userId);
	List<UserDto> getUsers(int page, int limit);

}
