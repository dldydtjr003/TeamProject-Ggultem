package com.honey.controller;

import com.honey.dto.ProcessedReportDTO;
import com.honey.service.ProcessedReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/admin/report")
@RequiredArgsConstructor
public class ProcessedReportController {

	private final ProcessedReportService processedService;

	@PostMapping("/process")
	public Map<String, Long> process(@RequestBody ProcessedReportDTO dto) {
		Long processedId = processedService.process(dto);
		return Map.of("PROCESSED_ID", processedId);
	}
}