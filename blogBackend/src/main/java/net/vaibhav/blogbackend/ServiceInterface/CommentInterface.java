package net.vaibhav.blogbackend.ServiceInterface;

import net.vaibhav.blogbackend.Entity.Comment;

public interface CommentInterface {
    void deleteComment(Long id);
    Comment addComment(Long blogId, Long userId, String commentText);
}
