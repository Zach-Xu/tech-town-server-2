package com.tech.model;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity(name = "Message")
@Table(name = "tb_message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdTime;

    @UpdateTimestamp
    private LocalDateTime updatedTime;

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
