package com.tech.model;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity(name = "Question")
@Table(name = "tb_question")
public class Question extends BaseEntity {

    @Column( name ="title", nullable = false, columnDefinition = "text")
    private String title;

    @Column( name ="content", nullable = false, columnDefinition = "LONGTEXT")
    private String content;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "fk_question_user"
            )
    )
    private User user;

    @OneToMany(
            mappedBy = "question",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST,CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<Answer> answers;

    @OneToMany(
            mappedBy = "question",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.EAGER
    )
    private List<Tag> tags;

    @Column( name="views", nullable = true, columnDefinition = "int default 0")
    private Integer views;

    @Column( name="up_votes", nullable = true, columnDefinition = "int default 0")
    private Integer upVotes;

    @Column( name="down_votes", nullable = true, columnDefinition = "int default 0")
    private Integer downVotes;

}
