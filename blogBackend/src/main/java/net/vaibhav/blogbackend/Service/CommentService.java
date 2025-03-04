package net.vaibhav.blogbackend.Service;

import lombok.extern.slf4j.Slf4j;
import net.vaibhav.blogbackend.Entity.Blog;
import net.vaibhav.blogbackend.Entity.Comment;
import net.vaibhav.blogbackend.Entity.Users;
import net.vaibhav.blogbackend.Repositories.BlogRepository;
import net.vaibhav.blogbackend.Repositories.CommentRepository;
import net.vaibhav.blogbackend.Repositories.UserRepository;
import net.vaibhav.blogbackend.ServiceInterface.CommentInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CommentService implements CommentInterface {
    private static final Logger log = LoggerFactory.getLogger(CommentService.class);
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private UserRepository userRepository;

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
    public Comment addComment(Long blogId, Long userId, String commentText) {
        try {
            Optional<Blog> blog =blogRepository.findById(blogId);
            Optional<Users> user = userRepository.findById(userId);
            System.out.println("Blog is Present"+" "+blog.isPresent());
            System.out.println("user is Present"+" "+user.isPresent());
            if(blog.isPresent() && user.isPresent()){
              Comment comment = new Comment();
              comment.setBlog(blog.get());
              comment.setUser(user.get());
                comment.setCommentText(commentText);
                comment.setCommentDate(LocalDateTime.now());
                return commentRepository.save(comment);

            }
        }catch (Exception e){
            System.out.println(e);

        }
    return  null;


    }
}
