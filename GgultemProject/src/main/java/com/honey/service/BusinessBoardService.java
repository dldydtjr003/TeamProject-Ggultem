package com.honey.service;

import java.time.LocalDate;
import java.util.List;

import com.honey.dto.BusinessBoardDTO;
import com.honey.dto.BusinessStatsDTO;
import com.honey.dto.PageResponseDTO;
import com.honey.dto.SearchDTO;

public interface BusinessBoardService {

	public Long register(BusinessBoardDTO businessBoardDTO);

	public BusinessBoardDTO get(Long no);

	public PageResponseDTO<BusinessBoardDTO> list(SearchDTO searchDTO);

	public PageResponseDTO<BusinessBoardDTO> deleteList(SearchDTO searchDTO);

	public void approve(Long no);

	public void modify(BusinessBoardDTO businessBoardDTO, BusinessBoardDTO oldBusinessBoardDTO);

	public void remove(Long no);

	public List<BusinessBoardDTO> adPSlist();

	public void viewCountAdd(Long no, String email);

	public List<BusinessBoardDTO> adPlList();

	public void reject(Long no);

	BusinessStatsDTO getStats(String email, String start, String end);

}
