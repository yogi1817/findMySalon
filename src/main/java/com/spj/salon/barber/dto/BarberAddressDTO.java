package com.spj.salon.barber.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class BarberAddressDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2555740855389742709L;
	private String firstName;
	private String lastName;
	private String middleName;
	private String storeName;
	private String email;
	private String phone;
	private String addressLineOne;
	private String addressLineTwo;
	private String city;
	private String state;
	private String zip;
	private Double distance;
	private String waitTime;
}