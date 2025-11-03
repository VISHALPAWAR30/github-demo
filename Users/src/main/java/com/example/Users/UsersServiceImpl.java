package com.example.Users;

import com.example.Users.Data.UserEntity;
import com.example.Users.Data.UserRepository;
import com.example.Users.Share.UserDto;
import com.example.Users.ui.Service.UsersService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UsersServiceImpl extends UsersService {

    UserRepository userRepository;

    @Autowired
    public UsersServiceImpl(UserRepository userRepository)
    {
        this.userRepository = userRepository;

    }

    public UserDto createUser(UserDto userDetails){
        userDetails.setUserId(UUID.randomUUID().toString());
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserEntity userEntity = modelMapper.map(userDetails,UserEntity.class);
        userEntity.setEncryptedPassword("test");



        userRepository.save(userEntity);
        return null;
    }

}
