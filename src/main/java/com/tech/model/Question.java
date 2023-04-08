package com.tech.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonIncludeProperties({"id","username","email","avatar"})
    private User user;

    @ManyToMany(
            cascade = {CascadeType.ALL},
            mappedBy = "bookmarks"
    )
    @JsonIncludeProperties({"id", "username", "avatar"})
    private List<User> bookmarkedUsers;

    @OneToMany(
            mappedBy = "question",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST,CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    @JsonManagedReference(value = "question-answer")
    private List<Answer> answers;

    @OneToMany(
            mappedBy = "question",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.EAGER
    )
    @JsonManagedReference(value = "question-tag")
    private List<Tag> tags;

    @Column( name="views", nullable = false, columnDefinition = "int default 0")
    private int views;

    @Column( name="up_votes", nullable = false, columnDefinition = "int default 0")
    private int upVotes;

    @Column( name="down_votes", nullable = false, columnDefinition = "int default 0")
    private int downVotes;

}
