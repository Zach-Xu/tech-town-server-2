package com.tech.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity( name = "Vote")
@Table( name = "tb_user_vote")
public class Vote extends BaseEntity{

    @ManyToOne
    @JoinColumn(
            name = "voted_user_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "fk_vote_user"
            )

    )
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(
            name ="voted_question_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                   name = "fk_vote_question"
            )
    )
    @JsonIgnore
    private Question question;

    @Column( name = "vote_status", columnDefinition = "smallint default 1 comment 'vote status, 0 for cancel, 1 for up vote, 2 for down vote'")
    private int status;

}
