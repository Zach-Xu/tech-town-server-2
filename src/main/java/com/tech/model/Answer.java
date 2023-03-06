package com.tech.model;

import lombok.Data;

import javax.persistence.*;

@Entity(name = "Answer")
@Table(name = " tb_answer")
public class Answer extends BaseEntity {

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(
            name = "question_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "fk_answer_question"
            )
    )
    private Question question;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "fk_answer_user"
            )
    )
    private User user;


}
