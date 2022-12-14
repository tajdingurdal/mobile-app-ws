package com.tajdingurdal.app.service.impl;

import java.util.ArrayList;
import java.util.List;
import com.tajdingurdal.app.entity.UserEntity;
import com.tajdingurdal.app.exception.ErrorMessages;
import com.tajdingurdal.app.exception.UserServiceException;
import com.tajdingurdal.app.repository.UserRepository;
import com.tajdingurdal.app.shared.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.tajdingurdal.app.service.UserService;
import com.tajdingurdal.app.shared.dto.UserDto;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    Utils utils;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public UserDto createUser(UserDto userDto) throws Exception {
        if (userRepository.existsByEmail(userDto.getEmail())) throw new Exception("Email already exist");

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userDto, userEntity);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        userEntity.setUserId(utils.generateUserId(30));

        UserEntity savedUser = userRepository.save(userEntity);

        UserDto result = new UserDto();
        BeanUtils.copyProperties(savedUser, result);
        log.debug("user dto: {}", result);
        return result;
    }

    @Override
    public UserDto getUserByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        log.debug("user entity: {}", userEntity);
        if (userEntity == null) throw new UsernameNotFoundException(email);

        UserDto result = new UserDto();
        BeanUtils.copyProperties(userEntity, result);
        return result;
    }

    @Override
    public UserDto getUserByUserId(String userId) {

        UserDto userDto = new UserDto();
        UserEntity userEntity = userRepository.findByUserId(userId);
        log.debug("user entity: {}", userEntity);
        if (userEntity == null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        BeanUtils.copyProperties(userEntity, userDto);

        return userDto;
    }

    @Override
    public UserDto updateUser(String id, UserDto userDto) {
        UserEntity userEntity = new UserEntity();

        UserDto userDtoFromDB = getUserByUserId(id);

        userDtoFromDB.setFirstName(userDto.getFirstName());
        userDtoFromDB.setLastName(userDto.getLastName());

        BeanUtils.copyProperties(userDtoFromDB, userEntity);
        userRepository.save(userEntity);
        log.debug("updated user: {}", userEntity);

        return userDtoFromDB;
    }

    @Override
    public void deleteUser(String userId) {

        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        log.debug("deleted user: {}", userEntity);
        userRepository.delete(userEntity);
    }

    @Override
    public List<UserDto> getUsers(int page, int limit) {
        List<UserDto> result = new ArrayList<>();

       // if (page > 0) page -= 1;

        PageRequest pageRequest = PageRequest.of(page, limit);
        Page<UserEntity> pageOfUsers = userRepository.findAll(pageRequest);
        List<UserEntity> userEntityList = pageOfUsers.getContent();

        for (UserEntity userEntity : userEntityList) {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(userEntity, userDto);
            result.add(userDto);
        }

        return result;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);
        log.debug("user entity: {}", userEntity);
        if (userEntity == null)
            throw new UsernameNotFoundException(email);


        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }

}
