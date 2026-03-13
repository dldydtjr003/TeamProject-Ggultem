package com.honey.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder // Builder 패턴 객체 생성
@AllArgsConstructor // 매개변수 자동 생성
@NoArgsConstructor // 매개변수 없는 기본 생성

public class BoardDTO {

	private Integer boardNo;

	private Long memberNo; // 작성자 회원번호

	private String title;

	private String writer;

	private String content;

	private Integer viewCount;

	private Integer enabled;

	// 새로 업로드할 파일들
	@Builder.Default
	private List<MultipartFile> files = new ArrayList<>();

	// 업로드 완료된 파일명들
	@Builder.Default
	private List<String> uploadFileNames = new ArrayList<>();

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime regDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime dtdDate;

}
