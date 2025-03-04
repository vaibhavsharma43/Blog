package net.vaibhav.blogbackend.Controller;

import net.vaibhav.blogbackend.Entity.Blog;
import net.vaibhav.blogbackend.Entity.BlogResponseDTO;
import net.vaibhav.blogbackend.Entity.Users;
import net.vaibhav.blogbackend.Repositories.UserRepository;
import net.vaibhav.blogbackend.Service.BlogService;
import net.vaibhav.blogbackend.Service.UserDetailsServiceImp;
import net.vaibhav.blogbackend.Service.UserService;
import net.vaibhav.blogbackend.utilis.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("Public")
public class PublicController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsServiceImp userDetailsServiceImp;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;


    @GetMapping("/blogs")
    public List<BlogResponseDTO> getAllBlogsWithStats() {
        System.out.println("blogs-with-stats");
        return blogService.getAllBlogsWithStats();
    }

    @PostMapping("/userLogin")
    public ResponseEntity<?> userLogin(@RequestBody Users user) {

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
            UserDetails userDetails= userDetailsServiceImp.loadUserByUsername(user.getUsername());
            String jwt =jwtUtil.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
        }


    }
    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@RequestBody Users user) {
        System.out.println("createUser");
        Users users = userRepository.findByusername(user.getUsername());
        if (users != null) {

            return ResponseEntity.badRequest().body("user already exists!");
        }

//        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");// Encrypt password
        boolean b = userService.CreateUser(user);
        System.out.println(user.getUsername() +"   "+user.getRole());


        return ResponseEntity.ok("user created successfully!");
    }

}
