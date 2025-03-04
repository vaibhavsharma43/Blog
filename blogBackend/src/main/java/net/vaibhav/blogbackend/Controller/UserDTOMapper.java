package net.vaibhav.blogbackend.Controller;

import net.vaibhav.blogbackend.Entity.UserDTO;
import net.vaibhav.blogbackend.Entity.Users;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserDTOMapper implements Function<Users, UserDTO> {
    @Override
    public UserDTO apply(Users users) {
        return new UserDTO(
                users.getId(),
                users.getUsername(),
                users.getEmail(),
                users.getCreatedAt(),
                users.getUpdatedAt(),
                users.getRole(),
                users.isBanned()
        );
    }
}