package com.honey.controller;

import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.honey.dto.BannerDTO;
import com.honey.dto.PageResponseDTO;
import com.honey.dto.SearchDTO;
import com.honey.service.BannerService;
import com.honey.util.CustomFileUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/admin/banner")
public class BannerController {
	
	private final BannerService bannerService;
	private final CustomFileUtil fileUtil;
	
	@PostMapping("/register")
	public Map<String, Long> register(BannerDTO bannerDTO) {
		
		List<MultipartFile> files = bannerDTO.getFiles();

		List<String> uploadFileNames = fileUtil.saveFiles(files);

		bannerDTO.setUploadFileNames(uploadFileNames);
		
		Long no = bannerService.register(bannerDTO);
		
		return Map.of("NO", no);
	}
	
	@GetMapping("/{no}")
	public BannerDTO getBanner(@PathVariable(name = "no") Long no) {
		return bannerService.get(no);
	}
	
	@GetMapping("/list")
	public PageResponseDTO<BannerDTO> list(SearchDTO searchDTO) {
	
		return bannerService.list(searchDTO);
	}
	
	@DeleteMapping("/{no}")
	public void removeBanner(@PathVariable(name = "no") Long no) {
		bannerService.remove(no);
	}
	
	@PutMapping("/modify/{no}")
	public Map<String, String> modify(@PathVariable(name = "no") Long no, BannerDTO bannerDTO) {
		bannerDTO.setNo(no);
		BannerDTO oldbannerDTO = bannerService.get(no);
		
		bannerService.modify(bannerDTO, oldbannerDTO);
		
		return Map.of("RESULT", "SUCCESS");
	}
	
	@GetMapping("/view/{fileName}")
	public ResponseEntity<Resource> viewFileGET(@PathVariable String fileName) {
		return fileUtil.getFile(fileName);
	}
	
}
