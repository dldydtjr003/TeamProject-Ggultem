package com.honey.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProcessedReportDTO {
    private Long reportId;      // 어떤 신고를 처리하는지 (FK)
    private String adminEmail;  // 처리하는 관리자 이메일 (Member PK)
    private String actionNote;  // 관리자 조치 내용
    private String reportStatus; // "접수", 처리완료"
    
    private Integer memberStatus; // member에서 정해놓은 enabled값(2,3,4 중 하나 선택)
}