package com.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.model.InAppNotification;

public interface InAppNotificationRepository extends JpaRepository<InAppNotification, Integer> {

	List<InAppNotification> findByAgentId(Integer agentId);

	List<InAppNotification> findByCustomerId(Integer customerId);

}
