package com.ecomerce.auth.dto;

import com.ecomerce.auth.model.Role;
import com.ecomerce.auth.model.Users;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterDTO {
    private String email;
    private String password;
    private Role role;
    private String firstName;
    private String lastName;

    public Users toUser() {
        Users users = new Users();
        users.setEmail(this.getEmail());
        users.setPassword(this.getPassword());
        users.setRole(this.getRole());
        users.setFirstName(this.getFirstName());
        users.setLastName(this.getLastName());
        return users;
    }
}
