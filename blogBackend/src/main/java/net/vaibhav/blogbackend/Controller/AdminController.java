package net.vaibhav.blogbackend.Controller;
import net.vaibhav.blogbackend.Entity.Blog;
import net.vaibhav.blogbackend.Entity.UserDTO;
import net.vaibhav.blogbackend.Entity.Users;
import net.vaibhav.blogbackend.Repositories.UserRepository;
import net.vaibhav.blogbackend.Service.UserDetailsServiceImp;
import net.vaibhav.blogbackend.ServiceInterface.BlogInterface;
import net.vaibhav.blogbackend.ServiceInterface.CommentInterface;
import net.vaibhav.blogbackend.ServiceInterface.UserInterface;
import net.vaibhav.blogbackend.utilis.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private BlogInterface blogService;

    @Autowired
    private UserInterface userInterface;

    @Autowired
    private CommentInterface commentService;


    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsServiceImp userDetailsServiceImp;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;

    //    Create Admin
@PostMapping("/createAdmin")
public ResponseEntity<?> createAdmin(@RequestBody Users admin) {
    Users users = userRepository.findByusername(admin.getUsername());
    if (users != null) {

        return ResponseEntity.badRequest().body("ADMIN already exists!");
    }


    admin.setRole("ADMIN");
    userInterface.CreateUser(admin);
    System.out.println(admin.getUsername() +"   "+admin.getRole());
    userRepository.save(admin);

    return ResponseEntity.ok("ADMIN created successfully!");
}
    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {

        try {
            boolean isDeleted = userInterface.deleteById(id);
            if (isDeleted) {
                return ResponseEntity.ok("User deleted successfully with ID: " + id);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete user with ID: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting user with ID: " + id);
        }
    }


    @PostMapping("/createPublisher")
    public ResponseEntity<?> createPublisher(@RequestBody Users user) {

        Users users = userRepository.findByusername(user.getUsername());
        if (users != null) {

            return ResponseEntity.badRequest().body("Publisher already exists!");
        }


        user.setRole("PUBLISHER");
        userInterface.CreateUser(user);
        System.out.println(user.getUsername() +"   "+user.getRole());
        userRepository.save(user);

        return ResponseEntity.ok("Publisher created successfully!");
    }

    // User Endpoints
    @GetMapping("/users")
    public List<UserDTO> getAllUsers() {
        return userInterface.getAllUsers();
    }

    @PostMapping("/users/ban/{id}")
    public String banUser(@PathVariable Long id) {
        userInterface.banUser(id);
        return "User banned with ID: " + id;
    }

    @PostMapping("/users/unban/{id}")
    public String unbanUser(@PathVariable Long id) {
        userInterface.unbanUser(id);
        return "User unbanned with ID: " + id;
    }

    // Comment Endpoints
    @DeleteMapping("/comments/{id}")
    public String deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return "Comment deleted with ID: " + id;
    }
    @GetMapping("/getPublisher")
    public List<UserDTO> getPublisher() {
        try {
            return userInterface.getPublisher();
        } catch (Exception e) {
            // Log the exception and return an empty list or handle it as needed
            e.printStackTrace();
            return Collections.emptyList();
        }
    }



}