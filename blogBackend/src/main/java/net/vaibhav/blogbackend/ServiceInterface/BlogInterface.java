package net.vaibhav.blogbackend.ServiceInterface;

import net.vaibhav.blogbackend.Entity.Blog;
import net.vaibhav.blogbackend.Entity.Users;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface BlogInterface {
    Blog updateBlog(Long id,Blog blog);
    List<Blog> getAllBlogs();
    Blog createBlog(Blog blog);
    void deleteBlog(Long id);
    List<Blog> getBlogsByPublisher(Users user);
    Blog getBlogById(Long id);
}
