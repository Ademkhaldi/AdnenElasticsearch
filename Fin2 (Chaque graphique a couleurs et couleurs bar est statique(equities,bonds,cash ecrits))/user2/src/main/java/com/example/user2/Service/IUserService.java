package com.example.user2.Service;

import com.example.user2.models.User;

import java.util.List;

public interface IUserService {


     List<User> getAllUsers();

     public boolean deleteUser(String id);


     User retrieveUser(String id);
     User updateUser(String id, User user);
}