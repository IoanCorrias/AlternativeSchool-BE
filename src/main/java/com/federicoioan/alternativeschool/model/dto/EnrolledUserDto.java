package com.federicoioan.alternativeschool.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrolledUserDto {

    private Long id;

    private String name;

    private String surname;

    private String username;

    private String email;

    private Boolean certificateEnabled;
}
