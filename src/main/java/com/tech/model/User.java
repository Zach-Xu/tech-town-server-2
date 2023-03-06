package com.tech.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@Entity(name = "User")
@Table(name = "tb_user")
public class User extends BaseEntity {

    @NotBlank(message = "username must be provided")
    @Column(name = "username", unique = true, nullable = false, columnDefinition = "VARCHAR(32)")
    private String username;

    @Email(message = "must be a valid email")
    @NotBlank(message = "email must be provided")
    @Column(name = "email", unique = true, nullable = false, columnDefinition = "VARCHAR(32)")
    private String email;

    @NotBlank(message = "password must be provided")
    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(
            mappedBy = "user",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<Question> questions;

    @OneToMany(
            mappedBy = "user",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<Answer> answers;
}
