package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired  // injecting dependencies at run time
    private UserRepository repository;

    public User createUser(User user){
        return repository.save(user);
    }

    public List<User> createUsers(List<User> users){
        return repository.saveAll(users);
    }
    public List<User> getUsers(){
        return repository.findAll();
    }



}
