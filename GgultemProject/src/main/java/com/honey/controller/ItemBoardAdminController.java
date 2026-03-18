package com.honey.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.honey.dto.ItemBoardAdminDTO;
import com.honey.dto.ItemBoardDTO;
import com.honey.dto.PageResponseDTO;
import com.honey.dto.SearchDTO;
import com.honey.service.ItemBoardAdminService;
import com.honey.util.CustomFileUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/admin/itemBoard")
public class ItemBoardAdminController {

	private final ItemBoardAdminService service;
	private final CustomFileUtil fileUtil;
	
	@GetMapping("/{id}")
	public ItemBoardAdminDTO getItemBoardAdmin(@PathVariable(name = "id") Long id) {
		return service.get(id);
	}
	
	@PostMapping("/")
    public Map<String, Long> register(@RequestBody ItemBoardDTO dto) {
        Long id = service.register(dto);
        return Map.of("id", id);
    }
	
	@GetMapping("/list")
	public PageResponseDTO<ItemBoardAdminDTO> list(SearchDTO searchDTO){
		return service.list(searchDTO);
	}
	
	@GetMapping("/delete/{id}")
	public Map<String,String> remove(@PathVariable(name = "id")Long id){
		List<String> oldFileNames = service.get(id).getUploadFileNames();
		service.remove(id);
		
		fileUtil.deleteFiles(oldFileNames);
		return Map.of("RESULT","SUCCESS");
	}
	
}