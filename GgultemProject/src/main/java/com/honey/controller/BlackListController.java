package com.honey.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.honey.dto.BlackListDTO;
import com.honey.dto.PageResponseDTO;
import com.honey.dto.SearchDTO;
import com.honey.service.BlackListService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/blacklist")
public class BlackListController {

	private final BlackListService service;
	
	@GetMapping("/{blId}")
	public BlackListDTO getBlackList(@PathVariable(name = "blId") Long blId) {
		return service.get(blId);
	}
	
	@PostMapping("/")
	public Map<String, Long> register(@RequestBody BlackListDTO blackListDTO) {
		Long blId = service.register(blackListDTO);
		return Map.of("blId", blId);
	}
	
	@GetMapping("/list")
	public PageResponseDTO<BlackListDTO> list(SearchDTO searchDTO) {
		return service.list(searchDTO);
	}
	
	@PutMapping("/{roomId}")
	public Map<String, String> modify(@PathVariable(name = "blId") Long blId, @RequestBody BlackListDTO blackListDTO) {
		blackListDTO.setBlId(blId);
		service.modify(blackListDTO);
		return Map.of("RESULT", "SUCCESS");
	}
	
	@DeleteMapping("/remove/{blId}")
	public Map<String, String> remove(@PathVariable(name = "blId") Long blId) {
		service.remove(blId);
		return Map.of("RESULT", "SUCCESS");
	}
	
}
	   


