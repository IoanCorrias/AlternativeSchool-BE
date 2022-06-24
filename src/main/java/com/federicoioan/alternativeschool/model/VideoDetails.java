package com.federicoioan.alternativeschool.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "videos")
public class VideoDetails {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String url;

    private String endPointVimeo;

    @ManyToOne
    @JoinColumn(name = "course_module_id")
    @JsonIgnore
    private CourseModule courseModule;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateInsert;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateUpdate;
}
