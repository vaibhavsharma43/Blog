package net.vaibhav.blogbackend.Entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data


public class LikeId implements Serializable {
    private Long userId;
    private Long blogId;

    public LikeId(Long userId, Long blogId) {
        this.userId = userId;
        this.blogId = blogId;
    }
    public LikeId() {
    }
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }
}
