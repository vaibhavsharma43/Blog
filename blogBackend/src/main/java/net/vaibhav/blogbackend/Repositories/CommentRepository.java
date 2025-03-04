package net.vaibhav.blogbackend.Repositories;

import net.vaibhav.blogbackend.Entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.blog.id = :blogId")
    int countCommentsByBlogId(Long blogId);
    List<Comment> findByBlogId(Long blogId);
}
