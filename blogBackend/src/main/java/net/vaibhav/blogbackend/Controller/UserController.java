package net.vaibhav.blogbackend.Controller;

import net.vaibhav.blogbackend.Entity.*;
import net.vaibhav.blogbackend.Repositories.BlogRepository;
import net.vaibhav.blogbackend.Repositories.LikeRepository;
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
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private BlogInterface blogService;

    @Autowired
    private UserInterface userService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentInterface commentService;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsServiceImp userDetailsServiceImp;

    @Autowired
    private CommentInterface commentInterface;
    private JwtUtil jwtUtil;
    @Autowired
    private LikeRepository likeRepository;

    @GetMapping("/blogs")
    public List<Blog> getAllBlogs() {

        return blogService.getAllBlogs();
    }

    @PostMapping("/userLogin")
    public ResponseEntity<?> userLogin(@RequestBody Users user) {
        System.out.println("USER this SIde");
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
        try {
            boolean response = userService.CreateUser(user);
            if (response) {
                return ResponseEntity.ok("User created successfully!");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User creation failed!");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
    @PutMapping("/toggleLike/{blogId}")
    public ResponseEntity<?> addLike(@PathVariable Long blogId, Principal principal) {
        System.out.println("toggleLike");
        try {
            String username = principal.getName();

            // Fetch the user from the database
            Users user = userRepository.findByusername(username);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found.");
            }

            // Fetch the blog from the database
            Blog blog = blogRepository.findById(blogId).orElse(null);
            if (blog == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Blog not found.");
            }

            // Create LikeId
            LikeId likeId = new LikeId(user.getId(), blogId);

            // Check if like already exists
            Optional<Like> existingLike = likeRepository.findById(likeId);

            if (existingLike.isPresent()) {
                // If already liked, unlike it
                likeRepository.delete(existingLike.get());
                blog.setLikesCount(Math.max(0, blog.getLikesCount() - 1)); // Ensure count doesn't go negative
                blogRepository.save(blog);
                return ResponseEntity.ok("Unliked the blog!");
            } else {
                // Create a new like entry
                Like newLike = new Like(likeId, user, blog, true);
                likeRepository.save(newLike);

                // Increase the blog's like count
                blog.setLikesCount(blog.getLikesCount() + 1);
                blogRepository.save(blog);

                return ResponseEntity.ok("Liked the blog!");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/addComment")
    public ResponseEntity<?> addComment(@RequestParam Long blogId, @RequestParam String commentText, Principal principal) {
        
        try {
            System.out.println("AddComment");
            String username = principal.getName();

            // Fetch user from database
            Users user = userRepository.findByusername(username);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized: Only user can create blogs.");
            }

            // Add the comment
            Comment comment = commentInterface.addComment(blogId, user.getId(), commentText);
            System.out.println("Blog Id"+" "+blogId);
            System.out.println("comment Text"+" "+commentText);

            if (comment != null) {
                return ResponseEntity.ok("Comment added successfully!");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to add comment!");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }


    // Comment Endpoints
    @DeleteMapping("/comments/{id}")
    public String deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return "Comment deleted with ID: " + id;
    }

}
