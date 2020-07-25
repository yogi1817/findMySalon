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

import com.spj.salon.utils.DateUtils;

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
	
	@Column(name = "user_id")
	private Long userId;
	
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
		if(createTimestamp==null) {
			this.createTimestamp= DateUtils.getCurrentTimestamp();
		}
		this.createTimestamp = createTimestamp;
	}
	
	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	@Override
	public String toString() {
		return "DailyBarbers [dailyId=" + dailyId + ", barbersDescription=" + barbersDescription + ", barbersCount="
				+ barbersCount + ", createTimestamp=" + createTimestamp + ", userId=" + userId
				+ "]";
	}
}
