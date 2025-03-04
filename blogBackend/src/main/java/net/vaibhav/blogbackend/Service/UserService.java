package net.vaibhav.blogbackend.Service;
import java.util.*;
import java.util.stream.Collectors;

import net.vaibhav.blogbackend.Controller.UserDTOMapper;
import net.vaibhav.blogbackend.Entity.Blog;
import net.vaibhav.blogbackend.Entity.UserDTO;
import net.vaibhav.blogbackend.Entity.Users;
import net.vaibhav.blogbackend.Repositories.BlogRepository;
import net.vaibhav.blogbackend.Repositories.UserRepository;
import net.vaibhav.blogbackend.ServiceInterface.UserInterface;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserInterface {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final PasswordEncoder passwordEncoder;
    private final UserDTOMapper userDTOMapper;
    private final UserRepository userRepository;
    private final BlogRepository blogRepository;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserDTOMapper userDTOMapper, UserRepository userRepository, BlogRepository blogRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userDTOMapper = userDTOMapper;
        this.userRepository = userRepository;
        this.blogRepository = blogRepository;
    }
    @Override
    public boolean deleteById(Long id) {
        try {
            // Fetch the user


            Users user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Dissociate blogs from the user
            List<Blog> blogs = blogRepository.findByAdmin(user);
            for (Blog blog : blogs) {
                blog.setAdmin(null);
                blogRepository.save(blog);
            }
            userRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            log.error("Error deleting user by id", e);
            return false;
        }
    }
    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userDTOMapper)
                .collect(Collectors.toList());
    }
    @Override
    public boolean CreateUser(Users user) {
        try {
            System.out.println("User Password is "+" "+user.getPassword());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            // Encrypt password
            userRepository.save(user);
            return true;

        }catch (Exception e){
            System.out.println(e);
            return  false;
        }


    }

    public void banUser(Long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setBanned(true); // Ensure this setter is available
        userRepository.save(user);
    }

    public void unbanUser(Long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setBanned(false);
        userRepository.save(user);
    }

    @Override
    public List<UserDTO> getPublisher() {
        try {
            return userRepository.findAll()
                    .stream()
                    .filter(user -> "PUBLISHER".equals(user.getRole()))
                    .map(userDTOMapper)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching publishers", e);
            return Collections.emptyList();
        }
    }

}