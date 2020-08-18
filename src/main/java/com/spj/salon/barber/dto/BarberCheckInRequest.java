package com.spj.salon.barber.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BarberCheckInRequest implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String zipCode;
	private Double longitude;
	private Double latitude;
	private Double distance;
}
