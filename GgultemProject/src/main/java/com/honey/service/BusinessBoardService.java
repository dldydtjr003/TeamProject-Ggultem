package com.honey.service;

import com.honey.dto.BusinessBoardDTO;
import com.honey.dto.MemberDTO;
import com.honey.dto.PageRequestDTO;
import com.honey.dto.PageResponseDTO;

public interface BusinessBoardService {

	public Long register(BusinessBoardDTO businessBoardDTO);

	public BusinessBoardDTO get(Long no);

	public PageResponseDTO<BusinessBoardDTO> list(PageRequestDTO pageRequestDTO);

	public void approve(Long no);

	public void modify(BusinessBoardDTO businessBoardDTO, BusinessBoardDTO oldBusinessBoardDTO);

	public void remove(Long no);

}
