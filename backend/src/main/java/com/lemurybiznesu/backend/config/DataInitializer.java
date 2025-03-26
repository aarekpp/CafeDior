package com.lemurybiznesu.backend.config;

import com.lemurybiznesu.backend.model.entity.ERole;
import com.lemurybiznesu.backend.model.entity.Role;
import com.lemurybiznesu.backend.model.entity.User;
import com.lemurybiznesu.backend.repository.RoleRepository;
import com.lemurybiznesu.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

@Configuration
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final static Logger LOGGER = LoggerFactory.getLogger(DataInitializer.class);

    public DataInitializer(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args){
        try{
            initializeRoles();
            initializeAdminAccount();
            LOGGER.info("Data initialized");
        } catch (Exception e) {
            LOGGER.error("Error while initializing data", e);
        }
    }

    private void initializeRoles(){
        Arrays.stream(ERole.values()).forEach(roleEnum -> {
            try{
                if(!roleRepository.existsByName(roleEnum)){
                    Role role = new Role();
                    role.setName(roleEnum);
                    roleRepository.save(role);
                }
            } catch (Exception e) {
                LOGGER.error("Error while initializing role {}",roleEnum, e);
            }
        });
    }

    private void initializeAdminAccount(){
        try{
            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Role admin not found"));
            List<User> adminUser = userRepository.findByRole(adminRole);
            if(adminUser.isEmpty()){
                User user = new User();
                user.setFirstName("Admin");
                user.setLastName("Admin");
                user.setEmail("admin@lemurybiznesu.com");
                user.setPhoneNumber("+123456789");
                user.setPassword(passwordEncoder.encode("ZAQ!2wsxcd"));
                user.setRole(adminRole);
                userRepository.save(user);
            }
        } catch (Exception e) {
            LOGGER.error("Error while initializing admin account", e);
        }
    }
}
