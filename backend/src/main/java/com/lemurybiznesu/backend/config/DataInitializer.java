package com.lemurybiznesu.backend.config;

import com.lemurybiznesu.backend.model.entity.ERole;
import com.lemurybiznesu.backend.model.entity.Role;
import com.lemurybiznesu.backend.model.entity.User;
import com.lemurybiznesu.backend.repository.RoleRepository;
import com.lemurybiznesu.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Configuration
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final static Logger LOGGER = LoggerFactory.getLogger(DataInitializer.class);

    @Value("${app.images.path}")
    private String imagesPath;

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
            initializeModeratorAccount();
            initializeImagesDirectory();
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
            Role adminRole = roleRepository.findByName(ERole.ADMIN).orElseThrow(() -> new RuntimeException("Role admin not found"));
            List<User> adminUser = userRepository.findByRole(adminRole);
            if(adminUser.isEmpty()){
                User user = new User();
                user.setFirstName("Admin");
                user.setLastName("Admin");
                user.setEmail("admin@lemurybiznesu.com");
                user.setPhoneNumber("+48123456789");
                user.setPassword(passwordEncoder.encode("ZAQ!2wsxcd"));
                user.setRole(adminRole);
                userRepository.save(user);
            }
        } catch (Exception e) {
            LOGGER.error("Error while initializing admin account", e);
        }
    }

    private void initializeModeratorAccount(){
        try{
            Role moderatorRole = roleRepository.findByName(ERole.MODERATOR).orElseThrow(() -> new RuntimeException("Role moderator not found"));
            List<User> moderatorUser = userRepository.findByRole(moderatorRole);
            if(moderatorUser.isEmpty()){
                User user = new User();
                user.setFirstName("Moderator");
                user.setLastName("Moderator");
                user.setEmail("moderator@lemurybiznesu.com");
                user.setPhoneNumber("+48112233444");
                user.setPassword(passwordEncoder.encode("ZAQ!2wsxcd"));
                user.setRole(moderatorRole);
                userRepository.save(user);
            }
        } catch (Exception e) {
            LOGGER.error("Error while initializing moderator account", e);
        }
    }

    private void initializeImagesDirectory() {
        Path path = Paths.get(imagesPath);
        try {
            Files.createDirectories(path);
            if (!Files.isWritable(path)) {
                throw new RuntimeException("No write permissions for: " + path.toAbsolutePath());
            }
        } catch (IOException e) {
            throw new RuntimeException("Directory initialization failed: " + path.toAbsolutePath(), e);
        }
    }
}
