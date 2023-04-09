package com.tech.model;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.tech.utils.ActionType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Table(name = "tb_activity")
@Entity(name = "Activity")
public class Activity extends BaseEntity {

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne
    @JoinColumn(
            name = "question_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "fk_activity_question"
            )
    )
    @JsonIncludeProperties({"id","username","title"})
    private Question question;

    @Enumerated(EnumType.STRING)
    @Column(name = "action")
    private ActionType action;

}
