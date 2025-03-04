package net.vaibhav.blogbackend;

import net.vaibhav.blogbackend.Entity.Users;
import net.vaibhav.blogbackend.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class AdminDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Debugging log
        System.out.println("Fetching user: " + username);

        // Fetch user from the database
        Users user = userRepository.findByusername(username);

        // If user is not found, log and throw exception
        if (user == null) {
            System.out.println("User not found in DB!");
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        System.out.println("User found: " + user.getUsername() + " Role: " + user.getRole());

        // Ensure the role is properly prefixed (avoid double prefixing)
        String role = user.getRole().startsWith("ROLE_") ? user.getRole() : "ROLE_" + user.getRole().toUpperCase();

        // Return UserDetails object
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername()) // Set username
                .password(user.getPassword()) // Set encoded password
                .authorities(Collections.singletonList(new SimpleGrantedAuthority(role))) // Assign correct role
                .build();
    }
}
