package com.honey.service;

import java.util.List;

import com.honey.dto.BoardReplyDTO;
import com.honey.dto.PageResponseDTO;
import com.honey.dto.SearchDTO;

public interface BoardReplyService {

    ///////////////////
    /// 일반 사용자
    //////////////////

    // 댓글 등록
    Long register(BoardReplyDTO boardReplyDTO);

    // 댓글 목록
    List<BoardReplyDTO> list(Integer boardNo);

    // 댓글 수정
    void modify(BoardReplyDTO boardReplyDTO);

    // 댓글 삭제
    void remove(Long replyNo);

    ///////////////////
    /// 관리자
    //////////////////

    // 관리자 댓글 목록 (페이징)
    PageResponseDTO<BoardReplyDTO> adminReplyList(SearchDTO searchDTO);


    // 관리자 댓글 삭제
    void adminRemove(Long replyNo);
}