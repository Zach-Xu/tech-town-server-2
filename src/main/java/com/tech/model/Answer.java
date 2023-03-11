package com.tech.model;

import com.azure.core.annotation.Get;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "Answer")
@Table(name = " tb_answer")
@Getter
@Setter
public class Answer extends BaseEntity {

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
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
    @JsonBackReference(value = "question-answer")
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
    @JsonIgnoreProperties({"password", "answers", "questions"})
    private User user;


}
