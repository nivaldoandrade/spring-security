package com.nasa.oauth2_resource_server.bootstrap;

import com.nasa.oauth2_resource_server.entities.User;
import com.nasa.oauth2_resource_server.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class createUser implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        createUserDefault();
    }

    private void createUserDefault() {
        String password = "123";
        String hashPassword = passwordEncoder.encode(password);

        User user = new User();
        user.setName("user");
        user.setEmail("user@mail.com");
        user.setPassword(hashPassword);

        User user1 = new User();
        user1.setName("user1");
        user1.setEmail("user1@mail.com");
        user1.setPassword(hashPassword);

       List<User> users = List.of(user, user1);

        userRepository.saveAll(users);

        System.out.println("Usuários de teste: ");
        for(User u: users) {
            System.out.println("email: " + u.getEmail());
            System.out.println("password: " + password);
        }


    }

}
