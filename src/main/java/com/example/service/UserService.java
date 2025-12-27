package com.example.service;

import com.example.mongodb.model.User;
import com.example.mongodb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }
    
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    public User createUser(User user) {
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }
    
    public User updateUser(String id, User userDetails) {
        return userRepository.findById(id)
            .map(user -> {
                user.setUsername(userDetails.getUsername());
                user.setEmail(userDetails.getEmail());
                user.setFirstName(userDetails.getFirstName());
                user.setLastName(userDetails.getLastName());
                user.setUpdatedAt(LocalDateTime.now());
                return userRepository.save(user);
            })
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
    
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
    
    public List<User> searchUsersByEmail(String emailPattern) {
        return userRepository.findByEmailRegex(emailPattern);
    }
}

