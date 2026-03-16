package com.honey.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.honey.dto.BlackListDTO;
import com.honey.repository.BlackListRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class BlackListServiceImpl implements BlackListService {
	private final ModelMapper modelMapper;
	private final BlackListRepository repository;
/*
	@Override
	public BlackListDTO get(Long blId) {
		Optional<BlackList> result = repository.findById(blId);
		BlackList blackList = result.orElseThrow();
		
		BlackListDTO blackListDTO = modelMapper.map(blackList, BlackListDTO.class);
		
		return blackListDTO;
	}
	
	@Override
	public Long register(BlackListDTO blackListDTO) {
		ChatRoom chatRoom = modelMapper.map(blackListDTO, ChatRoom.class);
		
		blackList.changeEnabled(1);
		
		return repository.save(blackList).getRoomId();
	}

	@Override
	public PageResponseDTO<ChatRoomDTO> list(PageRequestDTO pageRequestDTO) {
		Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, // 1 페이지가 0 이므로 주의
				pageRequestDTO.getSize(), Sort.by("roomId").descending());
		Page<ChatRoom> result = repository.findAllByEnabled(pageable);
		
		List<ChatRoomDTO> dtoList = result.getContent().stream().map(chatRoom -> {
			ChatRoomDTO dto = modelMapper.map(chatRoom, ChatRoomDTO.class);
	        return dto;
	    }).collect(Collectors.toList());

	long totalCount = result.getTotalElements();

	PageResponseDTO<ChatRoomDTO> responseDTO = PageResponseDTO.<ChatRoomDTO>withAll().dtoList(dtoList)
			.pageRequestDTO(pageRequestDTO).totalCount(totalCount).build();

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

	*/
	@Override
	public BlackListDTO get(Long blId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Long register(BlackListDTO blackListDTO) {
		// TODO Auto-generated method stub
		return null;
	}
}
