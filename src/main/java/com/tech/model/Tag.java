package com.tech.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "tb_tag")
@Entity(name = "Tag")
@Getter
@Setter
public class Tag extends BaseEntity {

    @Column(name = "tag_name", nullable = false, columnDefinition = "text")
    private String tagName;

    @Column(name = "description")
    private String description;

    @ManyToOne(
            cascade = CascadeType.PERSIST
    )
    @JoinColumn(
            name = "question_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "fk_tag_question"
            )

    )
    @JsonIgnore
    private Question question;
}
