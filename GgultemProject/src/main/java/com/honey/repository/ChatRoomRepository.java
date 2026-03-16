package com.honey.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.honey.domain.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
	
	@Query("select c from ChatRoom c where enabled = 1")
	Page<ChatRoom> findAllByEnabled(Pageable pageable);

	@Query("SELECT b FROM ChatRoom b WHERE " +
	       "( (:searchType = 'roomName' AND b.roomName LIKE %:keyword%) OR " +
	       "  (:searchType = 'buyerId' AND b.buyerId LIKE %:keyword%) OR " +
	       "  (:searchType = 'sellerId' AND b.sellerId LIKE %:keyword%) OR " +
	       "  (:searchType = 'all' AND (b.roomName LIKE %:keyword% OR b.buyerId LIKE %:keyword% OR b.sellerId LIKE %:keyword%)) ) " +
	       "OR " + // searchType이 없거나 비었을 때의 처리
	       "( (:searchType IS NULL OR :searchType = '') AND (b.roomName LIKE %:keyword% OR b.buyerId LIKE %:keyword% OR b.sellerId LIKE %:keyword%) )")
	Page<ChatRoom> searchByCondition(@Param("searchType")String searchType,@Param("keyword") String keyword, Pageable pageable);

}
