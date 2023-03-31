package com.qy.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {

    private Long id;

    private String name;

    private String password;

    private String content;

    private String desc;
}
