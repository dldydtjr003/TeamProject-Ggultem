package com.honey.service;

import com.honey.dto.ItemBoardAdminDTO;
import com.honey.dto.ItemBoardDTO;
import com.honey.dto.PageResponseDTO;
import com.honey.dto.SearchDTO;

public interface ItemBoardAdminService {

    public ItemBoardAdminDTO get(Long id);

    public Long register(ItemBoardDTO itemBoardDTO);

    public PageResponseDTO<ItemBoardAdminDTO> list(SearchDTO searchDTO);

    public void remove(Long id);

}