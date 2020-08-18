package com.spj.salon.barber.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BarberCheckInResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<BarberAddressDTO> barberAddressDTO;
	private String message;
}