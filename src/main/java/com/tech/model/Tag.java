package com.tech.model;

import javax.persistence.*;

@Table(name = "tb_tag")
@Entity(name = "Tag")
public class Tag extends BaseEntity {

    @Column(name = "tag_name", nullable = false, columnDefinition = "text")
    private String tagName;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(
            name = "question_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "fk_tag_question"
            )
    )
    private Question question;
}
