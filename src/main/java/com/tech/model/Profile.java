package com.tech.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity(name ="Profile")
@Table(name = "tb_profile")
public class Profile extends BaseEntity{

    @Column(name = "bio")
    private String bio;

    @Column(name = "user_id")
    private Long userId;

    @OneToMany(
            mappedBy = "profile",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE},
            fetch = FetchType.EAGER
    )
    @JsonManagedReference(value = "profile-skill")
    private List<Skill> skills;

}
