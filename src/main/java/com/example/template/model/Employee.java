package com.example.template.model;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString
@Accessors(chain = true)
public class Employee {
    private Long id;
    private String firstName;
    private String lastName;
    private String address;
}
