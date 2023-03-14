package com.tech.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
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
    @JsonIncludeProperties({"id", "username"})
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
    @JsonIncludeProperties({"id","title", "upVotes", "downVotes"})
    private Question question;

    @Column( name = "vote_status", columnDefinition = "smallint default 1 comment 'vote status, 0 for cancel, 1 for up vote, 2 for down vote'")
    private int status;

}
