package com.honey.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.honey.common.BaseTimeEntity;

@Entity
@Table(name = "tbl_banner")
@Getter
@ToString(exclude = "imageList")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Banner extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long no; // 배너 번호

	private String title; // 배너 제목

	private String content; // 배너 설명/내용

	private String link; // 배너 클릭 시 이동할 URL

	private LocalDateTime endDate; // 마감일

	@Builder.Default
	private int enabled = 1; // 1: 활성(운영), 0: 비활성(삭제)

	// 배너 이미지 관리를 위한 컬렉션 (이미지 파일명들)
	@ElementCollection
	@CollectionTable(name = "tbl_banner_image", joinColumns = @JoinColumn(name = "banner_no"))
	@Builder.Default
	private List<BannerImage> imageList = new ArrayList<>();

	// 수정 메서드 (Change 메서드)
	public void changeTitle(String title) {
		this.title = title;
	}

	public void changeContent(String content) {
		this.content = content;
	}

	public void changeLink(String link) {
		this.link = link;
	}

	public void changeEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	public void changeEnabled(int enabled) {
		this.enabled = enabled;
	}
	
	public void addImage(BannerImage image) {
		image.setOrd(this.imageList.size());
		imageList.add(image);
	}
	public void addImageString(String fileName) {
		BannerImage bItem = BannerImage.builder().fileName(fileName).build();
		addImage(bItem);
	}
	public void clearList() {
		this.imageList.clear();
	}
}