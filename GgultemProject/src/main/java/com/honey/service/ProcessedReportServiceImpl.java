package com.honey.service;

import com.honey.domain.Member;
import com.honey.domain.ProcessedReport;
import com.honey.domain.Report;
import com.honey.dto.ProcessedReportDTO;
import com.honey.repository.MemberRepository;
import com.honey.repository.ProcessedReportRepository;
import com.honey.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProcessedReportServiceImpl implements ProcessedReportService {

	private final ProcessedReportRepository processedRepository;
	private final ReportRepository reportRepository;
	private final MemberRepository memberRepository;

	@Override
	public Long process(ProcessedReportDTO dto) {
		// 1. 원본 신고 내역 조회
	    Report report = reportRepository.findById(dto.getReportId())
	            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 신고 내역입니다."));

	    // 2. 피신고자(신고 당한 사람) 조회
	    // Report 엔티티의 targetMemberId가 이메일이라고 하셨으니 이를 이용해 조회합니다.
	    Member targetMember = memberRepository.findById(report.getTargetMemberId())
	            .orElseThrow(() -> new IllegalArgumentException("피신고자를 찾을 수 없습니다."));

	    // 3. 관리자 객체 생성 (Email 기반)
	    Member admin = Member.builder().email(dto.getAdminEmail()).build();

	    // 4. ⭐ 정지 조치 실행 (Member 엔티티의 메서드 호출)
	    // dto.getSuspensionDays() 대신 이제는 엔티티 정의에 따른 '상태 번호'를 받습니다.
	    // 2: 7일 정지, 3: 30일 정지, 4: 영구 정지
	    if (dto.getMemberStatus() != null) {
	        targetMember.changeStatus(dto.getMemberStatus());
	    }

	    // 5. 처리 기록 저장
	    ProcessedReport processed = ProcessedReport.builder()
	            .report(report)
	            .admin(admin)
	            .actionNote(dto.getActionNote())
	            .reportStatus(dto.getReportStatus()) // "처리완료"
	            .build();
	    
	    processedRepository.save(processed);

		// 4. 원본 신고 상태 업데이트 (0:접수 -> 1:처리완료 등)
		// Integer 타입인 status에 맞춰 1(처리완료)로 설정
		report.changeStatus(1);

		return processed.getProcessedReportId();
	}
}