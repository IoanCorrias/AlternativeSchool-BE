package com.federicoioan.alternativeschool.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue
    private Integer id;

    @Enumerated(EnumType.STRING)
    private ERole name;

    public Role(ERole eRole) {
        this.name = eRole;
    }

    public static Role valueOf(String role) {
        return new Role(ERole.valueOf(role));
    }
}
