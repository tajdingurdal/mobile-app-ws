package com.tajdingurdal.app.contoller;


import com.tajdingurdal.app.model.request.RequestOperationName;
import com.tajdingurdal.app.model.request.RequestOperationStatus;
import com.tajdingurdal.app.model.request.UserDetailsRequestModel;
import com.tajdingurdal.app.model.response.OperationStatusModel;
import com.tajdingurdal.app.model.response.UserRest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.tajdingurdal.app.service.UserService;
import com.tajdingurdal.app.shared.dto.UserDto;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users") // http://localhost:8080/users
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {

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

    @PutMapping(path = "/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {

        UserRest result = new UserRest();
        UserDto userDto = new UserDto();
        if (userDetails.getLastName().isEmpty()) throw new NullPointerException("the lastname is empty");

        BeanUtils.copyProperties(userDetails, userDto);
        UserDto createUser = userService.updateUser(id, userDto);
        BeanUtils.copyProperties(createUser, result);

        return result;
    }

    @DeleteMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public OperationStatusModel deleteUser(@PathVariable String id) {

        OperationStatusModel operationStatusModel = new OperationStatusModel();
        operationStatusModel.setOperationName(RequestOperationName.DELETE.name());
        userService.deleteUser(id);
        operationStatusModel.setOperationResult(RequestOperationStatus.SUCCESS.name());

        return operationStatusModel;
    }

    // http://localhost:8080/users?page=1&limit=50
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "limit", defaultValue = "25") int limit) {

        List<UserRest> resultUserList = new ArrayList<>();
        List<UserDto> userDtos = userService.getUsers(page, limit);

        for (UserDto userDto : userDtos) {
            UserRest userRest = new UserRest();
            BeanUtils.copyProperties(userDto, userRest);
            resultUserList.add(userRest);
        }

        return resultUserList;
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }

}
