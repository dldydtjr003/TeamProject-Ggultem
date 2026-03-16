package com.honey.service;

import com.honey.dto.BlackListDTO;
import com.honey.dto.ChatRoomDTO;
import com.honey.dto.PageRequestDTO;
import com.honey.dto.PageResponseDTO;

public interface BlackListService {

	public BlackListDTO get(Long blId);

	public Long register(BlackListDTO blackListDTO);




	
	
}
