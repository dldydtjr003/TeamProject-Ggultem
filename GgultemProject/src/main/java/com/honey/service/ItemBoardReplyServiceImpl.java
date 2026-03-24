package com.honey.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.honey.domain.ItemBoard;
import com.honey.domain.ItemBoardReply;
import com.honey.domain.Member;
import com.honey.dto.ItemBoardReplyDTO;
import com.honey.repository.ItemBoardReplyRepository;
import com.honey.repository.ItemBoardRepository;
import com.honey.repository.MemberRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemBoardReplyServiceImpl implements ItemBoardReplyService {

	private final ItemBoardReplyRepository itemBoardReplyRepository;
	private final ItemBoardRepository itemBoardRepository;
	private final MemberRepository memberRepository;
	
	@Override
	public List<ItemBoardReplyDTO> list(Long id) {

		List<ItemBoardReply> replyList = itemBoardReplyRepository.getRepliesByItem(id);

		Map<Long, ItemBoardReplyDTO> dtoMap = replyList.stream()
				.map(reply -> ItemBoardReplyDTO.builder().replyNo(reply.getReplyNo()).id(reply.getItemBoard().getId())
						.email(reply.getMember().getEmail()).content(reply.getContent())
						.parentReplyNo(reply.getParent() != null ? reply.getParent().getReplyNo() : null)
						.enabled(1).build())
				.collect(Collectors.toMap(ItemBoardReplyDTO::getReplyNo, dto -> dto));

		List<ItemBoardReplyDTO> result = new ArrayList<>();

		for (ItemBoardReplyDTO dto : dtoMap.values()) {

			if (dto.getParentReplyNo() == null) {
				result.add(dto);
			} else {
				ItemBoardReplyDTO parent = dtoMap.get(dto.getParentReplyNo());
				if (parent != null) {
					parent.getChildList().add(dto);
				}
			}
		}

		return result;
	}

	@Override
	public Long register(ItemBoardReplyDTO dto) {
		ItemBoard itemBoard = itemBoardRepository.findById(dto.getId()).orElseThrow();
		
		Member member = memberRepository.findById(dto.getEmail()).orElseThrow();
		
		ItemBoardReply parentReply = null;
		if(dto.getParentReplyNo() != null) {
			parentReply = itemBoardReplyRepository.findById(dto.getParentReplyNo()).orElseThrow();
		}
		ItemBoardReply reply = ItemBoardReply.builder().itemBoard(itemBoard).member(member).content(dto.getContent())
				.parent(parentReply).enabled(1).build();
		itemBoardReplyRepository.save(reply);
		
		return reply.getReplyNo();
	}

}
