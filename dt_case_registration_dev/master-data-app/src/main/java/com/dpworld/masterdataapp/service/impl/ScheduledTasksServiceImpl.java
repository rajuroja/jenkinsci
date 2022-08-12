package com.dpworld.masterdataapp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dpworld.masterdataapp.service.ScheduledTasksService;
import com.dpworld.persistence.entity.ScheduledTasks;
import com.dpworld.persistence.repository.ScheduledTasksRepository;

@Service
public class ScheduledTasksServiceImpl implements ScheduledTasksService {

	@Autowired
	private ScheduledTasksRepository scheduledTasksRepository;

	@Override
	public ScheduledTasks findByScheduledTasksId(long id) {
		return scheduledTasksRepository.findByScheduledTasksId(id);
	}

	@Override
	public ScheduledTasks findByProcessName(String process) {
		return scheduledTasksRepository.findByProcessName(process);
	}

	@Override
	public void save(ScheduledTasks task) {
		scheduledTasksRepository.save(task);
	}

}