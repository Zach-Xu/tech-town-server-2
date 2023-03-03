package com.tech.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@Entity(name = "User")
@Table(name = "tb_user")
public class User extends BaseEntity {

    @NotNull(message = "username must be provided")
    @Column(name = "username", unique = true, nullable = false, columnDefinition = "VARCHAR(32)")
    private String username;

    @Email(message = "must be a valid email")
    @NotNull(message = "email must be provided")
    @Column(name = "email", unique = true, nullable = false, columnDefinition = "VARCHAR(32)")
    private String email;

    @NotNull(message = "password must be provided")
    @Column(name = "password", nullable = false)
    private String password;
}
