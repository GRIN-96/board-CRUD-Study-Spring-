package com.study.board.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity   // Model 생성
@Data
public class Board {
    @Id // PK
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // AI
    private Integer id;

    private String title;

    private String content;

}
