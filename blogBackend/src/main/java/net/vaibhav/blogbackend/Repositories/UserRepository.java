package net.vaibhav.blogbackend.Repositories;

import net.vaibhav.blogbackend.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByusername(String username);

}