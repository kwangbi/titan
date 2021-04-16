package com.greece.titan.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("person")
@Getter
@Setter
@ToString
public class RedisEntity implements Serializable {
    private static final long serialVersionUID = 1370692830319429806L;

    @Id
    private Long id;

    private String firstname;
    private String lastname;
    private int age;

}
