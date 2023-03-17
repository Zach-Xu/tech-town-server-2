package com.tech.dto;


import com.tech.utils.InboxType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDTO {

    private Long receiverId;

    private String content;

    private InboxType type;
}
