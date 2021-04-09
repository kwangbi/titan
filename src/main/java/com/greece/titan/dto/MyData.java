package com.greece.titan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyData implements Serializable {
    private static final long serialVersionUID = -7353484588260422449L;
    private String studentId;
    private String name;
}
