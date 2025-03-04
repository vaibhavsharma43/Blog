package net.vaibhav.blogbackend.Entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
@Data
public class BlogResponseDTO {
    private final String title;
    private Long id;
    private String imageUrl;
    private String description;
    private LocalDateTime createdAt;
    private int likeCount;
    private int commentCount;
    private List<String> comments; // Add this field


    public BlogResponseDTO(Long id, String title,String imageUrl, String description, LocalDateTime createdAt,
                           int likeCount, int commentCount, List<String> comments) {
        this.id = id;
        this.title=title;
        this.imageUrl = imageUrl;
        this.description = description;
        this.createdAt = createdAt;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.comments = comments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getTitle() {
        return title;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    // Getters and setters
}