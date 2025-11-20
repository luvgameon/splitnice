package com.splitnice.authservice.service;

import com.splitnice.authservice.entities.UserEvent;
import com.splitnice.authservice.eventProducer.UserInfoEvent;
import com.splitnice.authservice.eventProducer.UserInfoProducer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.splitnice.authservice.entities.UserInfo;
import com.splitnice.authservice.model.UserInfoDto;
import com.splitnice.authservice.repository.UserRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;

@Component
@AllArgsConstructor
@Data
public class UserDetailsServiceImpl implements UserDetailsService
{

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final UserInfoProducer userInfoProducer;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {


        UserInfo user = userRepository.findByUsername(username);
        if(user == null){

            throw new UsernameNotFoundException("could not found user..!!");
        }
        return new CustomUserDetails(user);
    }

    public UserInfo checkIfUserAlreadyExist(UserInfoDto userInfoDto){
        return userRepository.findByUsername(userInfoDto.getUsername());
    }

    public Boolean signupUser(UserInfoDto userInfoDto){
        //        ValidationUtil.validateUserAttributes(userInfoDto);
        userInfoDto.setPassword(passwordEncoder.encode(userInfoDto.getPassword()));
        if(Objects.nonNull(checkIfUserAlreadyExist(userInfoDto))){
            return false;
        }
        String userId = UUID.randomUUID().toString();
        UserInfo userInfo = new UserInfo(userId, userInfoDto.getUsername(), userInfoDto.getPassword(), new HashSet<>());
        userRepository.save(userInfo);
        // pushEventToQueue
        userInfoProducer.sendMessage(userInfoEventToPublish(userInfoDto, userId));
        return true;
    }

    private UserInfoEvent userInfoEventToPublish(UserInfoDto userInfoDto, String userId){
        return UserInfoEvent.builder()
                .userId(userId)
                .firstName(userInfoDto.getUsername())
                .lastName(userInfoDto.getLastName())
                .email(userInfoDto.getEmail())
                .phoneNumber(userInfoDto.getPhoneNumber()).build();

    }
}
