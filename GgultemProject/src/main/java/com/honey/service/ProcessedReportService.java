package com.honey.service;

import com.honey.dto.ProcessedReportDTO;

public interface ProcessedReportService {
	// 신고 처리 실행
	Long process(ProcessedReportDTO dto);
}