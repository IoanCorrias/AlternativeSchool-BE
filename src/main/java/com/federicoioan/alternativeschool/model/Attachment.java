package com.federicoioan.alternativeschool.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "attachments", uniqueConstraints = {
        @UniqueConstraint(columnNames = "code")
})
public class Attachment {

    @Id
    @GeneratedValue
    private Long id;

    private String code;

    private String name;

    private String type;

    private Long size;

    @ManyToOne
    @JoinColumn(name = "course_module_id")
    @JsonIgnore
    private CourseModule courseModule;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateInsert;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateUpdate;
}
