package com.federicoioan.alternativeschool.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDto {

	@NotBlank
	private String username;

	@NotBlank
	private String password;
}
