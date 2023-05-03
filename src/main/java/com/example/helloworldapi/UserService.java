package com.example.helloworldapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    public User addOrUpdate(User u){
        return userRepository.save(u);
    }

    public User getUser(String username){
        return userRepository.findById(username).orElse(null);
    }
}
