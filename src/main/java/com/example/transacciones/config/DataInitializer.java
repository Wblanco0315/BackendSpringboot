package com.example.transacciones.config;

import com.example.transacciones.models.users.RoleEntity;
import com.example.transacciones.repositories.PermissionRepository;
import com.example.transacciones.repositories.RoleRepository;
import com.example.transacciones.repositories.UserRepository;
import com.example.transacciones.services.RoleService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService role;

    @Override
    public void run(String... args) throws Exception {
        Long totalRoles = roleRepository.count();
        if (totalRoles <= 0) {
            RoleEntity adminRole = role.admin();
            RoleEntity clientRole = role.client();
            roleRepository.saveAll(Set.of(adminRole, clientRole));
        }

    }
}
