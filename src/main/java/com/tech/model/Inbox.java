package com.tech.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity(name = "Inbox")
@Table(name = "tb_inbox")
@Getter
@Setter
public class Inbox extends BaseEntity{

    @ManyToMany(
            mappedBy = "inboxes"
    )
    private List<User> participants;


    @OneToMany(
            cascade = {CascadeType.PERSIST,CascadeType.REMOVE},
            mappedBy = "inbox"
    )
    private List<Message> messages;

    @OneToOne
    @JoinColumn(
            name = "last_message_id",
            nullable = false,
            referencedColumnName = "id"
    )
    private Message lastMessage;
}
