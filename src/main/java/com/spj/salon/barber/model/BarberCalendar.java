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

/**
 * 
 * @author Yogesh Sharma
 *
 */
@Entity
@Table(name = "barber_calendar", schema = "usa", uniqueConstraints = @UniqueConstraint(columnNames = "barber_calendar_id"))
public class BarberCalendar implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3785966067355560454L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "barber_calendar_id", unique = true, nullable = false, columnDefinition = "serial")
	private Long barberCalendarId;
	
	@Column(name="barber_mapping_id")
	private Long barberMappingId;

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
	
	/**
	 * @return the barberCalendarId
	 */
	public Long getBarberCalendarId() {
		return barberCalendarId;
	}
	/**
	 * @param barberCalendarId the barberCalendarId to set
	 */
	public void setBarberCalendarId(Long barberCalendarId) {
		this.barberCalendarId = barberCalendarId;
	}
	/**
	 * @return the barberMappingId
	 */
	public Long getBarberMappingId() {
		return barberMappingId;
	}
	/**
	 * @param barberMappingId the barberMappingId to set
	 */
	public void setBarberMappingId(Long barberMappingId) {
		this.barberMappingId = barberMappingId;
	}
	/**
	 * @return the salonOpenTime
	 */
	public Date getSalonOpenTime() {
		return salonOpenTime;
	}
	/**
	 * @param salonOpenTime the salonOpenTime to set
	 */
	public void setSalonOpenTime(Date salonOpenTime) {
		this.salonOpenTime = salonOpenTime;
	}
	/**
	 * @return the salonCloseTime
	 */
	public Date getSalonCloseTime() {
		return salonCloseTime;
	}
	/**
	 * @param salonCloseTime the salonCloseTime to set
	 */
	public void setSalonCloseTime(Date salonCloseTime) {
		this.salonCloseTime = salonCloseTime;
	}
	/**
	 * @return the calendarDay
	 */
	public String getCalendarDay() {
		return calendarDay;
	}
	/**
	 * @param calendarDay the calendarDay to set
	 */
	public void setCalendarDay(String calendarDay) {
		this.calendarDay = calendarDay;
	}
	/**
	 * @return the calendarDate
	 */
	public Date getCalendarDate() {
		return calendarDate;
	}
	/**
	 * @param calendarDate the calendarDate to set
	 */
	public void setCalendarDate(Date calendarDate) {
		this.calendarDate = calendarDate;
	}
	/**
	 * @return the modifyDate
	 */
	public Date getModifyDate() {
		return modifyDate;
	}
	/**
	 * @param modifyDate the modifyDate to set
	 */
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	/**
	 * @return the salonOpensAt
	 */
	public String getSalonOpensAt() {
		return salonOpensAt;
	}
	/**
	 * @param salonOpensAt the salonOpensAt to set
	 */
	public void setSalonOpensAt(String salonOpensAt) {
		this.salonOpensAt = salonOpensAt;
	}
	/**
	 * @return the salonClosesAt
	 */
	public String getSalonClosesAt() {
		return salonClosesAt;
	}
	/**
	 * @param salonClosesAt the salonClosesAt to set
	 */
	public void setSalonClosesAt(String salonClosesAt) {
		this.salonClosesAt = salonClosesAt;
	}
	
	/**
	 * @return the calendayDateString
	 */
	public String getCalendayDateString() {
		return calendayDateString;
	}
	/**
	 * @param calendayDateString the calendayDateString to set
	 */
	public void setCalendayDateString(String calendayDateString) {
		this.calendayDateString = calendayDateString;
	}
	@Override
	public String toString() {
		return "BarberCalendar [barberCalendarId=" + barberCalendarId + ", barberMappingId=" + barberMappingId
				+ ", salonOpenTime=" + salonOpenTime + ", salonCloseTime=" + salonCloseTime + ", calendarDay="
				+ calendarDay + ", calendayDateString=" + calendayDateString + ", calendarDate=" + calendarDate
				+ ", modifyDate=" + modifyDate + ", salonOpensAt=" + salonOpensAt + ", salonClosesAt=" + salonClosesAt
				+ "]";
	}
}
