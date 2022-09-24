package com.tajdingurdal.app.contoller;


import com.tajdingurdal.app.exception.UserServiceException;
import com.tajdingurdal.app.model.request.UserDetailsRequestModel;
import com.tajdingurdal.app.exception.ErrorMessages;
import com.tajdingurdal.app.model.response.UserRest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.tajdingurdal.app.service.UserService;
import com.tajdingurdal.app.shared.dto.UserDto;

@RestController
@RequestMapping("/users") // http://localhost:8080/users
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
                 produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
        UserRest returnValue = new UserRest();

        UserRest result = new UserRest();
        UserDto userDto = new UserDto();
        if (userDetails.getLastName().isEmpty()) throw new NullPointerException("the lastname is empty");

        BeanUtils.copyProperties(userDetails, userDto);
        UserDto createUser = userService.createUser(userDto);
        BeanUtils.copyProperties(createUser, result);

        return result;
    }

    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserRest getUser(@PathVariable String id) {

        UserRest result = new UserRest();

        UserDto userDto = userService.getUserByUserId(id);
        BeanUtils.copyProperties(userDto, result);

        return result;

    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }

}
