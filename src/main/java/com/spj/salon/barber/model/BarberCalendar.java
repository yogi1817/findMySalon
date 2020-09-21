package com.spj.salon.barber.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import lombok.Builder;
import lombok.Data;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@Entity
@Table(name = "barber_calendar", schema = "usa", uniqueConstraints = @UniqueConstraint(columnNames = "barber_calendar_id"))
@Data
@Builder(toBuilder = true)
public class BarberCalendar implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3785966067355560454L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "barber_calendar_id", unique = true, nullable = false, columnDefinition = "serial")
	private Long barberCalendarId;
	
	@Column(name="user_id")
	private Long userId;

	private Date salonOpenTime;
	private Date salonCloseTime;
	private String calendarDay;
	
	@Transient
	private String calendayDateString;
	private Date calendarDate;//only populate if you wanted to overwrite the calendar day like 4th July.
	private Date modifyDate;
	
	//Date format "20:30 PM"
	@Transient
	private String salonOpensAt;
	@Transient
	private String salonClosesAt;
}
