package net.vaibhav.blogbackend.Controller;
import lombok.extern.slf4j.Slf4j;
import net.vaibhav.blogbackend.Entity.Blog;
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
import java.util.List;

@RestController
@RequestMapping("/Test")
@Slf4j
public class Test {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImp userDetailsServiceImp;
    @Autowired
    private BlogInterface blogService;

    @Autowired
    private UserInterface userService;

    @Autowired
    private CommentInterface commentService;



    @Autowired
    private final JwtUtil jwtUtil;

    public Test(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, UserDetailsServiceImp userDetailsServiceImp, JwtUtil jwtUtil) {
        this.userRepository = userRepository;

        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsServiceImp = userDetailsServiceImp;
        this.jwtUtil = jwtUtil;
    }

    // Blog Endpoints
    @GetMapping("/blogs")
    public List<Blog> getAllBlogs() {

        return blogService.getAllBlogs();
    }
    @PostMapping("/createBlog")
    public ResponseEntity<String> createBlog(@RequestBody Blog blog, Principal principal) {
        try {
            // Get the logged-in admin's username from the JWT token
            String adminUsername = principal.getName();

            // Fetch the admin details from the database
            Users publisher = userRepository.findByusername(adminUsername);
            if (publisher == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized: Only admins can create blogs.");
            }

            // Set the logged-in admin as the creator of the blog
            blog.setAdmin(publisher);

            // Save the blog
            Blog createdBlog = blogService.createBlog(blog);
            return ResponseEntity.ok("Blog created with Admin ID: " + publisher.getId());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
    @PostMapping("/createAdmin")
    public ResponseEntity<?> createAdmin(@RequestBody Users admin) {

        Users admin1 = userRepository.findByusername(admin.getUsername());
        if (admin1 != null) {
            return ResponseEntity.badRequest().body("Admin already exists!");
        }

//        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setRole("ADMIN");
       // Encrypt password
        boolean b = userService.CreateUser(admin);

        return ResponseEntity.ok("Admin created successfully!");
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
    @PostMapping("/publisherLogin")
    public ResponseEntity<?> publisherLogin(@RequestBody Users user) {

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
            UserDetails userDetails= userDetailsServiceImp.loadUserByUsername(user.getUsername());
            String jwt =jwtUtil.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
        }


    }



    @PostMapping("/adminlogin")
    public ResponseEntity<?> login(@RequestBody Users admin) {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(admin.getUsername(),admin.getPassword()));
            UserDetails userDetails= userDetailsServiceImp.loadUserByUsername(admin.getUsername());
            String jwt =jwtUtil.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        }catch (Exception e){

            return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
        }

    }
}