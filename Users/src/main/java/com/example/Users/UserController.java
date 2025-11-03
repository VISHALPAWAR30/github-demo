package com.example.Users;

import com.example.Users.Share.UserDto;
import com.example.Users.ui.Service.UsersService;
import com.example.Users.ui.model.CreateUserResponseModel;
import com.example.Users.ui.model.CreateUsersRequestModel;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private Environment env;

    @Autowired
    UsersService usersService;
//    private Object source;

    @GetMapping("/status/check")
    public String status() {
        return "working on port" + env.getProperty("local.server.port");
    }

    @PostMapping("/create")
    public ResponseEntity<CreateUserResponseModel>createdUser(
            @Valid @RequestBody CreateUsersRequestModel userDetails){

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = modelMapper.map(userDetails,UserDto.class);
       UserDto createUser= usersService.createUser(userDto);
       CreateUserResponseModel returnValue = modelMapper.map(createUser,CreateUserResponseModel.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
    }
}
