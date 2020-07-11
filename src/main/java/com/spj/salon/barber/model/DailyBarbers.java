package com.spj.salon.barber.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@Entity
@Table(name = "daily_barbers", schema = "usa", 
	uniqueConstraints = @UniqueConstraint(columnNames = "daily_id"))
public class DailyBarbers implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7027628947547933472L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "daily_id", unique = true, nullable = false, columnDefinition = "serial")
	private Long dailyId;
	
	private String barbersDescription;
	private int barbersCount;
	private LocalDateTime createTimestamp;
	
	@Column(name = "barber_mapping_id")
	private Long barbersMappingId;
	
	/**
	 * @return the dailyId
	 */
	public Long getDailyId() {
		return dailyId;
	}
	/**
	 * @param dailyId the dailyId to set
	 */
	public void setDailyId(Long dailyId) {
		this.dailyId = dailyId;
	}
	/**
	 * @return the barberDescription
	 */
	public String getBarbersDescription() {
		return barbersDescription;
	}
	/**
	 * @param barberDescription the barberDescription to set
	 */
	public void setBarbersDescription(String barbersDescription) {
		this.barbersDescription = barbersDescription;
	}
	/**
	 * @return the barberCount
	 */
	public int getBarbersCount() {
		return barbersCount;
	}
	/**
	 * @param barberCount the barberCount to set
	 */
	public void setBarbersCount(int barbersCount) {
		this.barbersCount = barbersCount;
	}
	/**
	 * @return the createTimestamp
	 */
	public LocalDateTime getCreateTimestamp() {
		return createTimestamp;
	}
	/**
	 * @param createTimestamp the createTimestamp to set
	 */
	public void setCreateTimestamp(LocalDateTime createTimestamp) {
		this.createTimestamp = createTimestamp;
	}
	
	/**
	 * @return the barbersMappingId
	 */
	public Long getBarbersMappingId() {
		return barbersMappingId;
	}
	/**
	 * @param barbersMappingId the barbersMappingId to set
	 */
	public void setBarbersMappingId(Long barbersMappingId) {
		this.barbersMappingId = barbersMappingId;
	}
	
	@Override
	public String toString() {
		return "DailyBarbers [dailyId=" + dailyId + ", barbersDescription=" + barbersDescription + ", barbersCount="
				+ barbersCount + ", createTimestamp=" + createTimestamp + ", barbersMappingId=" + barbersMappingId
				+ "]";
	}
}
