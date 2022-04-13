package com.example.users.services;

import com.example.users.usersRepository.User;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public interface UserService {
    void enter();
    void print();
    List<User> collectUsers();
}
