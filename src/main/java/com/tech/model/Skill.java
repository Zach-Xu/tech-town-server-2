package com.tech.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Table(name = "tb_skill")
@Entity(name = "Skill")
public class Skill extends BaseEntity{

    @Column(name = "skill_name", nullable = false, columnDefinition = "text")
    private String skillName;

    @Column(name = "description")
    private String description;

    @ManyToOne(
            cascade = CascadeType.PERSIST
    )
    @JoinColumn(
            name = "user_id",
            nullable = false,
            referencedColumnName = "user_id",
            foreignKey = @ForeignKey(
                    name = "fk_skill_user"
            )
    )
    @JsonBackReference(value = "profile-skill")
    private Profile profile;
}

