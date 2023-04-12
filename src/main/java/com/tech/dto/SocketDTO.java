package com.tech.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocketDTO {

    private String actionType;

    private Long roomId;

    private String message;

}
