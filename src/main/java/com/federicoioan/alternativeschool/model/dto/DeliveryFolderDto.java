package com.federicoioan.alternativeschool.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryFolderDto {

    @NotNull
    @Size(min = 3, max = 20)
    private String name;

    @NotNull
    private LocalDateTime startDeliveryTime;

    @NotNull
    private LocalDateTime endDeliveryTime;
}
