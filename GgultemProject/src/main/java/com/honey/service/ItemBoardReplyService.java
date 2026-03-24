package com.honey.service;

import java.util.List;

import com.honey.dto.ItemBoardReplyDTO;

public interface ItemBoardReplyService {

	public List<ItemBoardReplyDTO> list(Long id);

	public Long register(ItemBoardReplyDTO dto);

}
