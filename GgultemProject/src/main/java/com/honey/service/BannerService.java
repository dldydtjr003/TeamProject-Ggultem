package com.honey.service;

import com.honey.dto.BannerDTO;
import com.honey.dto.PageResponseDTO;
import com.honey.dto.SearchDTO;

public interface BannerService {
	public Long register(BannerDTO bannerDTO);

	public BannerDTO get(Long no);

	public PageResponseDTO<BannerDTO> list(SearchDTO searchDTO);
	
	public void modify(BannerDTO bannerDTO, BannerDTO oldBannerDTO);
	
	public void remove(Long no);
}
