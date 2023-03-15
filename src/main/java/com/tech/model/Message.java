package com.tech.model;

import lombok.Getter;
import lombok.Setter;

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
    private User sender;

    @ManyToOne
    @JoinColumn(
            name = "receiver_id",
            nullable = false,
            referencedColumnName = "id"
    )
    private User receiver;

    @ManyToOne
    @JoinColumn(
            name = "inbox_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "fk_message_inbox"
            )
    )
    private Inbox inbox;

    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String content;
}
