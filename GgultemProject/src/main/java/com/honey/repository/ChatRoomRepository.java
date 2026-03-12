package com.honey.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.honey.domain.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

}
