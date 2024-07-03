package com.ecomerce.auth.model;

import jakarta.persistence.*;
import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true, length = 50)
    private String email;
    @Column(length = 1024)
    private String password;
    private Role role;
    @Column(length = 50)
    private String firstName;
    @Column(length = 50)
    private String lastName;
}
