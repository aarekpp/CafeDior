package com.lemurybiznesu.backend.config;

import com.lemurybiznesu.backend.model.entity.ERole;
import com.lemurybiznesu.backend.model.entity.Role;
import com.lemurybiznesu.backend.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final static Logger LOGGER = LoggerFactory.getLogger(DataInitializer.class);

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args){
        try{
            initializeRoles();
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
}
