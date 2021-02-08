package com.web.curation.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScheduleParticipant {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int spno;
	
	private int sno;
	private int uno;
	private int attendance;//0:미정, 1:참가, 2:불참

}
