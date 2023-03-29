package com.tech.dto;

import com.tech.utils.InboxType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InboxDTO {

    private Long userId;

    private InboxType type;
}
