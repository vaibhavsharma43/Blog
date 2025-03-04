package net.vaibhav.blogbackend.Service;
import java.util.*;
import java.util.stream.Collectors;

import net.vaibhav.blogbackend.Entity.Blog;
import net.vaibhav.blogbackend.Entity.BlogResponseDTO;
import net.vaibhav.blogbackend.Entity.Comment;
import net.vaibhav.blogbackend.Entity.Users;
import net.vaibhav.blogbackend.Repositories.BlogRepository;
import net.vaibhav.blogbackend.Repositories.CommentRepository;
import net.vaibhav.blogbackend.Repositories.LikeRepository;
import net.vaibhav.blogbackend.ServiceInterface.BlogInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class BlogService implements BlogInterface {
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private CommentRepository commentRepository;
    private static final Logger logger = LoggerFactory.getLogger(BlogService.class);
    @Override
    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }
@Override
    public Blog createBlog(Blog blog) {
        return blogRepository.save(blog);
    }
   @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog existingBlog = blogRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Blog not found"));

        existingBlog.setImageUrl(blog.getImageUrl());
        existingBlog.setDescription(blog.getDescription());
        return blogRepository.save(existingBlog);
    }

    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    @Override
    public List<Blog> getBlogsByPublisher(Users publisher) {
        try {
            return blogRepository.findByAdmin(publisher);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch blogs for publisher: " + e.getMessage());
        }
    }

    @Override
    public Blog getBlogById(Long id) {
        try{
            return blogRepository.findById(id).get();
        } catch (Exception e){
            throw  new RuntimeException("Failed to fetch blog with Id "  +id +"  "+e.getMessage());
        }
    }


    public List<BlogResponseDTO> getAllBlogsWithStats() {
        List<Blog> blogs = blogRepository.findAll();
        logger.debug("Fetching all blogs with stats");

        return blogs.stream().map(blog -> {
            int likeCount = likeRepository.countLikesByBlogId(blog.getId());
            int commentCount = commentRepository.countCommentsByBlogId(blog.getId());
            logger.debug("Blog ID: {} | Likes: {} | Comments: {}", blog.getId(), likeCount, commentCount);

            List<String> commentTexts = commentRepository.findByBlogId(blog.getId())
                    .stream()
                    .map(Comment::getCommentText)
                    .collect(Collectors.toList());

            return new BlogResponseDTO(
                    blog.getId(),
                    blog.getTitle(),
                    blog.getImageUrl(),
                    blog.getDescription(),
                    blog.getCreatedAt(),
                    likeCount,
                    commentCount,
                    commentTexts
            );
        }).collect(Collectors.toList());
    }
}

