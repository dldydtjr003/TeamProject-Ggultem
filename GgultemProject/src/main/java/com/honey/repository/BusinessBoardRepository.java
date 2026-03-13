package com.honey.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.honey.domain.BusinessBoard;

public interface BusinessBoardRepository extends JpaRepository<BusinessBoard, Long> {
	
}
