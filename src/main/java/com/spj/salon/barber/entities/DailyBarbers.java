package com.spj.salon.barber.entities;

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

import lombok.*;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@Entity
@Table(name = "daily_barbers", schema = "usa", 
	uniqueConstraints = @UniqueConstraint(columnNames = "daily_id"))
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
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
	 * @param createTimestamp the createTimestamp to set
	 */
	public void setCreateTimestamp(LocalDateTime createTimestamp) {
		if(createTimestamp==null) {
			this.createTimestamp= DateUtils.getCurrentTimestamp();
		}
		this.createTimestamp = createTimestamp;
	}
}
