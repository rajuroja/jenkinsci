package com.dpworld.masterdataapp.service;

import com.dpworld.persistence.entity.ScheduledTasks;

public interface ScheduledTasksService {

	ScheduledTasks findByScheduledTasksId(long id);

	ScheduledTasks findByProcessName(String process);

	void save(ScheduledTasks task);

}
