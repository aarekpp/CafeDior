package com.lemurybiznesu.backend.service;

import com.lemurybiznesu.backend.model.dto.request.UserDetailsUpdateRequest;
import com.lemurybiznesu.backend.model.entity.User;
import com.lemurybiznesu.backend.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AuthService authService;

    public UserService(UserRepository userRepository, AuthService authService) {
        this.userRepository = userRepository;
        this.authService = authService;
    }

    public User getUser(String userId, HttpServletRequest request) {
        User currentUser = authService.getCurrentUser(request);
        if(!currentUser.getId().toString().equals(userId)) {
            throw new UsernameNotFoundException(userId);
        }
        return currentUser;
    }

    @Transactional
    public User updateUserDetails(String userId, HttpServletRequest request, UserDetailsUpdateRequest updateData) {
        User currentUser = authService.getCurrentUser(request);
        if(!currentUser.getId().toString().equals(userId)) {
            throw new UsernameNotFoundException(userId);
        }
        currentUser.setFirstName(updateData.getFirstName());
        currentUser.setLastName(updateData.getLastName());
        currentUser.setPhoneNumber(updateData.getPhoneNumber());

        try{
            return userRepository.save(currentUser);
        }catch(Exception e){
            return null;
        }
    }
}
