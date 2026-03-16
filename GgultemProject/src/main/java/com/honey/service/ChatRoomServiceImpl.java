package com.honey.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.honey.domain.ChatRoom;
import com.honey.dto.ChatRoomDTO;
import com.honey.dto.PageRequestDTO;
import com.honey.dto.PageResponseDTO;
import com.honey.dto.SearchDTO;
import com.honey.repository.ChatRoomRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {
	private final ModelMapper modelMapper;
	private final ChatRoomRepository repository;

	@Override
	public ChatRoomDTO get(Long roomId) {
		Optional<ChatRoom> result = repository.findById(roomId);
		ChatRoom chatRoom = result.orElseThrow();
		
		ChatRoomDTO chatRoomDTO = modelMapper.map(chatRoom, ChatRoomDTO.class);
		
		return chatRoomDTO;
	}
	
	@Override
	public Long register(ChatRoomDTO chatRoomDTO) {
		ChatRoom chatRoom = modelMapper.map(chatRoomDTO, ChatRoom.class);
		
		chatRoom.changeEnabled(1);
		
		return repository.save(chatRoom).getRoomId();
	}

	@Override
	public PageResponseDTO<ChatRoomDTO> list(SearchDTO searchDTO) {
		Pageable pageable = PageRequest.of(searchDTO.getPage() - 1, // 1 페이지가 0 이므로 주의
				searchDTO.getSize(), Sort.by("roomId").descending());
		Page<ChatRoom> result = repository.findAllByEnabled(pageable);
		if(searchDTO.getKeyword() != null && !searchDTO.getKeyword().isEmpty()) {
        	result = repository.searchByCondition(
					searchDTO.getSearchType(),
					searchDTO.getKeyword(),
					pageable);
        } else {
        	result = repository.findAllByEnabled(pageable);
        }
		List<ChatRoomDTO> dtoList = result.getContent().stream().map(chatRoom -> {
			ChatRoomDTO dto = modelMapper.map(chatRoom, ChatRoomDTO.class);
	        return dto;
	    }).collect(Collectors.toList());

	long totalCount = result.getTotalElements();

	PageResponseDTO<ChatRoomDTO> responseDTO = PageResponseDTO.<ChatRoomDTO>withAll().dtoList(dtoList)
			.pageRequestDTO(searchDTO).totalCount(totalCount).build();

	return responseDTO;
	}
	
	@Override
	public void modify(ChatRoomDTO chatRoomDTO) {
		Optional<ChatRoom> result = repository.findById(chatRoomDTO.getRoomId());
		ChatRoom chatRoom = result.orElseThrow();

		chatRoom.changeRoomName(chatRoomDTO.getRoomName());

	    repository.save(chatRoom);
	}
	
	@Override
	public void remove(Long roomId) {
		Optional<ChatRoom> result = repository.findById(roomId);
		ChatRoom chatRoom = result.orElseThrow();
		
		chatRoom.changeEnabled(0);

		repository.save(chatRoom);
	}

	
}
