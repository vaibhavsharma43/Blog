package net.vaibhav.blogbackend.ServiceInterface;

import net.vaibhav.blogbackend.Entity.UserDTO;
import net.vaibhav.blogbackend.Entity.Users;

import java.util.List;

public interface UserInterface {
    List<UserDTO> getAllUsers();

    boolean CreateUser(Users user);
    void banUser(Long id);
    void unbanUser(Long id);
    List<UserDTO>getPublisher();
    boolean deleteById(Long id);
}
