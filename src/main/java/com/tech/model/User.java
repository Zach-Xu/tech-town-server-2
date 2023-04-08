package com.tech.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @Column(name = "avatar", columnDefinition = "text comment 'user GitHub avatar url'")
    private String avatar;

    @OneToMany(
            mappedBy = "user",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    @JsonIgnoreProperties("user")
    private List<Question> questions;

    @OneToMany(
            mappedBy = "user",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    @JsonIgnoreProperties("user")
    private List<Answer> answers;

    @ManyToMany(
            cascade = CascadeType.PERSIST
    )
    @JoinTable(
            name = "user_inbox",
            joinColumns = @JoinColumn(
                    name = "user_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "inbox_id",
                    referencedColumnName = "id"
            )
    )
    @JsonIncludeProperties("user")
    private List<Inbox> inboxes;

    @OneToMany(
            mappedBy = "sender",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<Message> receivedMessages;

    @OneToMany(
            mappedBy = "receiver",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<Message> sentMessages;
}
