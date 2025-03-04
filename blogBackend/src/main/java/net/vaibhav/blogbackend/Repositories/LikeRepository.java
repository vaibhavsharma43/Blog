package net.vaibhav.blogbackend.Repositories;

import net.vaibhav.blogbackend.Entity.Like;
import net.vaibhav.blogbackend.Entity.LikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, LikeId> {
    @Query("SELECT COUNT(l) FROM Like l WHERE l.blog.id = :blogId")
    int countLikesByBlogId(Long blogId);

}
