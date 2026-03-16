package com.honey.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlackListDTO {
	private Long blId;
	private String userId;
	private String reason;
	private String adminId;
	private String status; // 활성 상태 (Y/N)
	private LocalDateTime startDate; 
	private LocalDateTime endDate; // (NULL이면 영구)
	private Integer enabled; // 1:활성화, 0:삭제
}