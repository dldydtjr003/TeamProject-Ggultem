package com.honey.domain;

import java.time.LocalDateTime;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProcessedReport {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROCESSED_REPORT_SEQ_GEN")
    private Long processedReportId; // 신고처리 번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_email") // 조치한 관리자
    private Member admin;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id") // 신고 번호
    private Report report;

    private String actionNote;  // 조치 내용
    private String reportStatus; // 처리완료, 진행중 등
    
    @Column(updatable = false)
    private LocalDateTime processedAt;

    @PrePersist
    public void prePersist() {
        this.processedAt = LocalDateTime.now();
    }
}

