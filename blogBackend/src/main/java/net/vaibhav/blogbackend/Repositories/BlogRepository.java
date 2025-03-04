package net.vaibhav.blogbackend.Repositories;
import net.vaibhav.blogbackend.Entity.Blog;
import net.vaibhav.blogbackend.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog,Long> {
    List<Blog> findByAdmin(Users admin);
}
