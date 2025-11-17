package com.splitnice.authservice.service;

import com.splitnice.authservice.eventProducer.UserInfoProducer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.splitnice.authservice.entities.UserInfo;
import com.splitnice.authservice.model.UserInfoDto;
import com.splitnice.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;
@Service
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository ;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserInfoProducer userInfoProducer;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserInfo user = userRepository.findByUsername(username);
    if(user == null){
        throw new UsernameNotFoundException("Could not find user");
    }
    return new CustomUserDetails(user);

    }

    public UserInfo checkUserAlreadyExists(UserInfoDto userInfoDto){
        return userRepository.findByUsername(userInfoDto.getUsername());
        }

        public boolean signupUser(UserInfoDto userInfoDto){
        if(Objects.nonNull(checkUserAlreadyExists(userInfoDto))){
            return false;
        }
        userInfoDto.setPassword(passwordEncoder.encode(userInfoDto.getPassword()));
        String userId = UUID.randomUUID().toString();
        userRepository.save(new UserInfo(userId,userInfoDto.getUsername(),userInfoDto.getPassword(),
                new HashSet<>()));
        //pusheventToKafka
    userInfoProducer.sendMessage(userInfoDto);
        return true;
    }
}
