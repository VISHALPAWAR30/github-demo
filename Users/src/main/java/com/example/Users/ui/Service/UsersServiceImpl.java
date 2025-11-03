package com.example.Users.ui.Service;

import com.example.Users.Data.UserEntity;
import com.example.Users.Share.UserDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.UUID;

public class UsersServiceImpl extends UsersService {
     @Override
    public UserDto ctreateUser(UserDto userDetails){
          userDetails.setUserId(UUID.randomUUID().toString());
         ModelMapper modelMapper=new ModelMapper();
         modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
         UserEntity userEntity = modelMapper.map(userDetails,UserEntity.class);
          return null;
     }
}
