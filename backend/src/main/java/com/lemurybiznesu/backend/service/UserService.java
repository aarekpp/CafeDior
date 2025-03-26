package com.lemurybiznesu.backend.service;

import com.lemurybiznesu.backend.model.entity.User;
import com.lemurybiznesu.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void incrementTokenVersion(UUID id) {
        try{
            User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            user.setTokenVersion(user.getTokenVersion() + 1);
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
