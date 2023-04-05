package com.tech.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity(name = "Profile")
@Table(name = "tb_profile",
    uniqueConstraints = {
        @UniqueConstraint(
                name = "unique_profile_user_id",
                columnNames = "user_id"
        )
    }
)
public class Profile extends BaseEntity implements Serializable {

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

    public Profile() {

    }

    public Profile(Long userId) {
        this.userId = userId;
    }

}
