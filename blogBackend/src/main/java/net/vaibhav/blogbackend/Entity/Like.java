package net.vaibhav.blogbackend.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "likes")
@Data
public class Like {

    @EmbeddedId
    private LikeId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId") // Map the userId field from LikeId
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("blogId") // Map the blogId field from LikeId
    @JoinColumn(name = "blog_id", insertable = false, updatable = false)
    @JsonIgnore
    private Blog blog;

    public LikeId getId() {
        return id;
    }

    public void setId(LikeId id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }
    public  Like(){

    }

    public Like(LikeId id, Users user, Blog blog, boolean isLiked) {
        this.id = id;
        this.user = user;
        this.blog = blog;
        this.isLiked = isLiked;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    private boolean isLiked;


}
