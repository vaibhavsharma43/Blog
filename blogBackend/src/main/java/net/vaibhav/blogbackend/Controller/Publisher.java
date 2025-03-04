package net.vaibhav.blogbackend.Controller;

import net.vaibhav.blogbackend.Entity.Blog;
import net.vaibhav.blogbackend.Entity.Users;
import net.vaibhav.blogbackend.Repositories.UserRepository;
import net.vaibhav.blogbackend.Service.BlogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.security.Principal;

@RestController
@RequestMapping("/Publisher")
public class Publisher {


    private final BlogService blogService;
    private final UserRepository userRepository;

    public Publisher( BlogService blogService, UserRepository userRepository) {

        this.blogService = blogService;
        this.userRepository = userRepository;
    }

    @PostMapping("/createBlog")
    public ResponseEntity<String> createBlog(@RequestBody Blog blog, Principal principal) {
        try {
            // Get the logged-in admin's username from the JWT token
            String adminUsername = principal.getName();

            // Fetch the admin details from the database
            Users Publisher = userRepository.findByusername(adminUsername);
            if (Publisher == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized: Only Publisher can create blogs.");
            }

            // Set the logged-in admin as the creator of the blog
            blog.setAdmin(Publisher);

            // Save the blog
            Blog createdBlog = blogService.createBlog(blog);
            return ResponseEntity.ok("Blog created with Publisher ID: " + Publisher.getId());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
    @PostMapping("/createBlogs")
    public ResponseEntity<?> createBlogs(@RequestBody List<Blog> blogs, Principal principal) {
        try {
            // Get the logged-in admin's username from the JWT token
            String adminUsername = principal.getName();

            // Fetch the admin details from the database
            Users publisher = userRepository.findByusername(adminUsername);
            if (publisher == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized: Only Publishers can create blogs.");
            }

            // List to store created blogs
            List<Blog> createdBlogs = new ArrayList<>();

            // Process each blog
            for (Blog blog : blogs) {
                blog.setAdmin(publisher);
                Blog savedBlog = blogService.createBlog(blog);
                createdBlogs.add(savedBlog);
            }

            return ResponseEntity.ok("Successfully created " + createdBlogs.size() + " blogs.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + e.getMessage());
        }
    }



    @PutMapping("/blogs/{id}")
    public ResponseEntity<String> updateBlog(@PathVariable  Long id ,@RequestBody Blog blogDetails, Principal principal) {
        try {


            Blog blog = blogService.updateBlog(id,blogDetails);


            return ResponseEntity.ok("Blog updated with ID: " + blog.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
    @DeleteMapping("/blogs/{id}")
    public ResponseEntity<String> deleteBlog(@PathVariable Long id) {

        blogService.deleteBlog(id);
        return ResponseEntity.ok("Blog deleted with ID: " + id);
    }

    @GetMapping("/myBlogs")
    public ResponseEntity<?> getMyBlogs(Principal principal) {
        try {
            String publisherUsername = principal.getName();
            Users publisher = userRepository.findByusername(publisherUsername);

            if (publisher == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized: Only registered publishers can access blogs.");
            }

            List<Blog> myBlogs = blogService.getBlogsByPublisher(publisher);

            if (myBlogs.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No blogs found for this publisher.");
            }

            return ResponseEntity.ok(myBlogs);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching blogs: " + e.getMessage());
        }
    }




}
