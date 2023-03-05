package com.tech.model;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity(name = "Question")
@Table(name = "tb_question")
public class Question extends BaseEntity {

    private String title;

    private String content;

}
