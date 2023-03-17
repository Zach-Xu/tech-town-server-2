package com.tech.model;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "Message")
@Table(name = "tb_message")
public class Message extends BaseEntity {

    @ManyToOne
    @JoinColumn(
            name = "sender_id",
            nullable = false,
            referencedColumnName = "id"
    )
    @JsonIncludeProperties({"id","username"})
    private User sender;

    @ManyToOne
    @JoinColumn(
            name = "receiver_id",
            nullable = false,
            referencedColumnName = "id"
    )
    @JsonIncludeProperties({"id","username"})
    private User receiver;

    @ManyToOne(
            cascade = CascadeType.PERSIST
    )
    @JoinColumn(
            name = "inbox_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "fk_message_inbox"
            )
    )
    @JsonIncludeProperties("id")
    private Inbox inbox;

    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String content;


}
