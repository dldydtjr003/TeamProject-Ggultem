package com.honey.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.honey.domain.Banner;
import com.honey.domain.BusinessBoard;
import com.honey.dto.BannerDTO;
import com.honey.dto.BusinessBoardDTO;
import com.honey.dto.MemberDTO;
import com.honey.dto.PageResponseDTO;
import com.honey.dto.SearchDTO;
import com.honey.repository.BannerRepository;
import com.honey.util.CustomFileUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class BannerServiceImpl implements BannerService {
	private final ModelMapper modelMapper;
	private final CustomFileUtil fileUtil;
	private final BannerRepository bannerRepository;

	@Override
	public Long register(BannerDTO bannerDTO) {
		
		Banner banner = Banner.builder().title(bannerDTO.getTitle())
				.content(bannerDTO.getContent()).link(bannerDTO.getLink())
				.endDate(bannerDTO.getEndDate())
				.enabled(1).build();

		// 파일 처리
		List<String> newFileNames = bannerDTO.getUploadFileNames();
		if (newFileNames != null && !newFileNames.isEmpty()) {
			banner.clearList(); // 초기화 후 추가
			newFileNames.forEach(banner::addImageString);
		}
		
		return bannerRepository.save(banner).getNo();
	}

	@Override
	public BannerDTO get(Long no) {
		Optional<Banner> result = bannerRepository.findById(no);
		Banner banner = result.orElseThrow();

		BannerDTO bannerDTO = modelMapper.map(banner, BannerDTO.class);

		List<String> fileNameList = banner.getImageList().stream().map(image -> image.getFileName())
				.collect(Collectors.toList());

		if (fileNameList != null && !fileNameList.isEmpty()) {
			bannerDTO.setUploadFileNames(fileNameList);
		}

		return bannerDTO;
	}

	@Override
	public PageResponseDTO<BannerDTO> list(SearchDTO searchDTO) {
		Pageable pageable = PageRequest.of(searchDTO.getPage() - 1, // 1 페이지가 0 이므로 주의
				searchDTO.getSize(), Sort.by("no").descending());

		log.info(searchDTO.toString());

		Page<Banner> result = null;
		if (searchDTO.getKeyword() != null && !searchDTO.getKeyword().isEmpty()) {
			if (searchDTO.getEnabled() != null) {
				result = bannerRepository.searchByConditionFilter(searchDTO.getSearchType(), searchDTO.getKeyword(),
						Integer.parseInt(searchDTO.getEnabled()), pageable);
			} else {
				result = bannerRepository.searchByCondition(searchDTO.getSearchType(), searchDTO.getKeyword(),
						pageable);
			}
		} else if (searchDTO.getEnabled() != null) {
			result = bannerRepository.findAllFilter(pageable, Integer.parseInt(searchDTO.getEnabled()));
		} else {
			result = bannerRepository.findAll(pageable);
		}

		List<BannerDTO> dtoList = result.getContent().stream().map(banner -> {
			BannerDTO dto = modelMapper.map(banner, BannerDTO.class);

			List<String> fileNameList = banner.getImageList().stream().map(image -> image.getFileName())
					.collect(Collectors.toList());

			if (fileNameList != null && !fileNameList.isEmpty()) {
				dto.setUploadFileNames(fileNameList);
			} else {
				dto.setUploadFileNames(List.of("default.jpg"));
			}

			dto.setEnabled(banner.getEnabled());

			return dto;
		}).collect(Collectors.toList());

		long totalCount = result.getTotalElements();

		PageResponseDTO<BannerDTO> responseDTO = PageResponseDTO.<BannerDTO>withAll().dtoList(dtoList)
				.pageRequestDTO(searchDTO).totalCount(totalCount).build();

		log.info(responseDTO.toString());

		return responseDTO;
	}

	@Override
	public void modify(BannerDTO bannerDTO, BannerDTO oldBannerDTO) {
		List<String> oldFileNames = oldBannerDTO.getUploadFileNames();

		List<MultipartFile> files = bannerDTO.getFiles();

		List<String> currentUpdateFileNames = null;
		if (files != null && !files.isEmpty()) {
			currentUpdateFileNames = fileUtil.saveFiles(files);
		}

		List<String> uploadFileNames = bannerDTO.getUploadFileNames();

		if (currentUpdateFileNames != null && !currentUpdateFileNames.isEmpty()) {
			uploadFileNames.addAll(currentUpdateFileNames);
		}

		bannerDTO.setUploadFileNames(uploadFileNames);

		Banner banner = bannerRepository.findById(bannerDTO.getNo()).orElseThrow();

		banner.clearList();

		List<String> newFileNames = bannerDTO.getUploadFileNames();
		if (newFileNames != null && !newFileNames.isEmpty()) {
			newFileNames.forEach(fileName -> {
				banner.addImageString(fileName);
			});
		}

		banner.changeTitle(bannerDTO.getTitle());
		banner.changeContent(bannerDTO.getContent());
		banner.changeLink(bannerDTO.getLink());
		banner.changeEnabled(bannerDTO.getEnabled());
		banner.changeEndDate(bannerDTO.getEndDate());
		

		bannerRepository.save(banner);

		if (oldFileNames != null && !oldFileNames.isEmpty()) {
			List<String> removeFiles = oldFileNames.stream().filter(fileName -> uploadFileNames.indexOf(fileName) == -1)
					.collect(Collectors.toList());
			fileUtil.deleteFiles(removeFiles);
		}
		
	}

	@Override
	public void remove(Long no) {
		Banner banner = bannerRepository.findById(no).orElseThrow();

		List<String> oldFileNames = banner.getImageList().stream().map(image -> image.getFileName())
				.collect(Collectors.toList());

		if (oldFileNames != null && !oldFileNames.isEmpty()) {
			fileUtil.deleteFiles(oldFileNames);
		}

		banner.clearList();

		bannerRepository.delete(banner);
	}

}
