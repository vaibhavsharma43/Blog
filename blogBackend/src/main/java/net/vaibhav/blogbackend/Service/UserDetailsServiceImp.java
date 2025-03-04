package net.vaibhav.blogbackend.Service;

import net.vaibhav.blogbackend.Entity.Users;
import net.vaibhav.blogbackend.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByusername(username);
        if (user != null) {
            System.out.println("Loaded user: " + user.getUsername() + " with role: " + user.getRole()); // Debugging log
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRole())
                    .build();
        }
        throw new UsernameNotFoundException(username);

    }
}