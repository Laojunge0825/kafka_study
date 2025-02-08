package com.shuke.springbootkafka.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements Serializable {


    private Integer id;

    private String name;

    private Integer age;

    private final static long serialVersionUID = 1L;


}
