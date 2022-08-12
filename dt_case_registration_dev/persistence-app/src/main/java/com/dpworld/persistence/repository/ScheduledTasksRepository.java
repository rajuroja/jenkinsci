package com.dpworld.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dpworld.persistence.entity.ScheduledTasks;

@Repository
public interface ScheduledTasksRepository extends JpaRepository<ScheduledTasks, Long> {

	ScheduledTasks findByScheduledTasksId(Long id);
	
	ScheduledTasks findByProcessName(String process);

}
