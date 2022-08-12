package com.dpworld.persistence.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "SCHEDULED_TASK", indexes = @Index(columnList = "PROCESS_NAME"))
public class ScheduledTasks implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "SCHEDULED_TASK_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_scheduled_task")
	@SequenceGenerator(name = "seq_scheduled_task", sequenceName = "seq_scheduled_task", allocationSize = 1)
	private long scheduledTasksId;

	@Column(name = "PROCESS_NAME", length = 50)
	private String processName;

	@Enumerated(EnumType.STRING)
	private StatusType statusType;

	@Column(name = "RUN_TYPE")
	private Character runtype;

	@Column(name = "REASON")
	private String reason;

	@Column(name = "STARTED_ON")
	private LocalDateTime startedOn;

	@Column(name = "COMPLETED_ON")
	private LocalDateTime completedOn;

	public long getScheduledTasksId() {
		return scheduledTasksId;
	}

	public void setScheduledTasksId(long scheduledTasksId) {
		this.scheduledTasksId = scheduledTasksId;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public StatusType getStatusType() {
		return statusType;
	}

	public void setStatusType(StatusType statusType) {
		this.statusType = statusType;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Character getRuntype() {
		return runtype;
	}

	public void setRuntype(Character runtype) {
		this.runtype = runtype;
	}

	public LocalDateTime getStartedOn() {
		return startedOn;
	}

	public void setStartedOn(LocalDateTime startedOn) {
		this.startedOn = startedOn;
	}

	public LocalDateTime getCompletedOn() {
		return completedOn;
	}

	public void setCompletedOn(LocalDateTime completedOn) {
		this.completedOn = completedOn;
	}

}
