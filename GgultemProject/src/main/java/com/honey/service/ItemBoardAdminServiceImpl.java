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

import com.honey.domain.ItemBoard;
import com.honey.dto.ItemBoardAdminDTO;
import com.honey.dto.ItemBoardDTO;
import com.honey.dto.PageResponseDTO;
import com.honey.dto.SearchDTO;
import com.honey.repository.ItemBoardAdminRepository;
import com.honey.repository.ItemBoardRepository;
import com.honey.util.CustomFileUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ItemBoardAdminServiceImpl implements ItemBoardAdminService {

	private final ModelMapper modelMapper;
	private final ItemBoardAdminRepository itemBoardAdminRepository;
	private final ItemBoardRepository itemBoardRepository;
	private final CustomFileUtil fileUtil;

	@Override
	public ItemBoardAdminDTO get(Long id) {
		// 1. Repository에서 데이터 찾기
		Optional<ItemBoard> result = itemBoardAdminRepository.findById(id);
		ItemBoard itemBoard = result.orElseThrow(() -> new RuntimeException("해당 상품을 찾을 수 없습니다."));

		// 2. Entity -> DTO 변환 (ModelMapper 사용 시)
		// 주의: 목적지 클래스를 ItemBoardAdminDTO.class로 정확히 지정해야 합니다.
		ItemBoardAdminDTO dto = modelMapper.map(itemBoard, ItemBoardAdminDTO.class);

		
		
		// 3. 이미지 파일명 리스트 처리
		// ItemBoard 엔티티 안에 이미지가 담긴 리스트(예: itemList)가 있다고 가정합니다.
		if (itemBoard.getItemList() != null) {
			List<String> fileNameList = itemBoard.getItemList().stream().map(itemImage -> itemImage.getFileName())
					.collect(Collectors.toList());

			if (!fileNameList.isEmpty()) {
				dto.setUploadFileNames(fileNameList);
			} else {
				dto.setUploadFileNames(List.of("default.jpg"));
			}
		}
		return dto;
	}

	@Override
	public Long register(ItemBoardDTO itemBoardDTO) {
		ItemBoard itemBoard = dtoToEntity(itemBoardDTO);
		ItemBoard result = itemBoardRepository.save(itemBoard);
		return result.getId();
	}

	private ItemBoard dtoToEntity(ItemBoardDTO itemBoardDTO) {
		ItemBoard itemBoard = ItemBoard.builder().title(itemBoardDTO.getTitle()).writer(itemBoardDTO.getWriter())
				.price(itemBoardDTO.getPrice()).content(itemBoardDTO.getContent()).category(itemBoardDTO.getCategory())
				.location(itemBoardDTO.getLocation()).itemUrl(itemBoardDTO.getItemUrl())
				.pictureUrl(itemBoardDTO.getPictureUrl()).enabled(1).status("판매중").build();
		// 업로드 처리가 끝난 파일들의 이름 리스트
		List<String> uploadFileNames = itemBoardDTO.getUploadFileNames();
		if (uploadFileNames == null) {
			return itemBoard;
		}
		uploadFileNames.stream().forEach(uploadName -> {
			itemBoard.addImageString(uploadName);
		});

		return itemBoard;
	}

	@Override
	public PageResponseDTO<ItemBoardAdminDTO> list(SearchDTO searchDTO) {
		Pageable pageable = PageRequest.of(searchDTO.getPage() - 1, searchDTO.getSize(), Sort.by("id").descending());

		Page<ItemBoard> result = null;

		if (searchDTO.getKeyword() != null && !searchDTO.getKeyword().isEmpty()) {
			result = itemBoardAdminRepository.searchByCondition(searchDTO.getSearchType(), searchDTO.getKeyword(),
					pageable);
		} else {
			result = itemBoardAdminRepository.findAllList(pageable);
		}

		List<ItemBoardAdminDTO> dtoList = result.getContent().stream()
				.map(itemBoard -> modelMapper.map(itemBoard, ItemBoardAdminDTO.class)).collect(Collectors.toList());

		long totalCount = result.getTotalElements();

		return PageResponseDTO.<ItemBoardAdminDTO>withAll().dtoList(dtoList).pageRequestDTO(searchDTO)
				.totalCount(totalCount).build();
	}

	@Override
	public void remove(Long id) {
		Optional<ItemBoard> result = itemBoardAdminRepository.findById(id);
		ItemBoard itemBoard = result.orElseThrow();
		
		itemBoard.changeEnabled(0);

		itemBoardAdminRepository.save(itemBoard);
		
	}

}