package com.honey.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.honey.domain.ItemBoard;

public interface ItemBoardRepository extends JpaRepository<ItemBoard, Long> {

	@Query("select i from ItemBoard i where enabled = 0")
	Page<ItemBoard> findAllList(Pageable pageable);

}
