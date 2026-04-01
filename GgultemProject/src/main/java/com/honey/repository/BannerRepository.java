package com.honey.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.honey.domain.Banner;

public interface BannerRepository extends JpaRepository<Banner, Long> {

	@Query("SELECT b FROM Banner b WHERE " + "( (:searchType = 'title' AND b.title LIKE %:keyword%) OR "
			+ "  (:searchType = 'content' AND b.content LIKE %:keyword%) OR "
			+ "  (:searchType = 'all' AND (b.title LIKE %:keyword% OR b.content LIKE %:keyword%)) ) " + "OR "
			+ "( (:searchType IS NULL OR :searchType = '') AND (b.title LIKE %:keyword% OR b.content LIKE %:keyword%) )")
	Page<Banner> searchByCondition(@Param("searchType") String searchType, @Param("keyword") String keyword,
			Pageable pageable);

	@Query("SELECT b FROM Banner b WHERE " +
	           "( " +
	           "  (:searchType = 'title' AND b.title LIKE %:keyword% AND b.enabled = :enabled) OR " +
	           "  (:searchType = 'content' AND b.content LIKE %:keyword% AND b.enabled = :enabled) OR " +
	           "  (:searchType = 'all' AND (b.title LIKE %:keyword% OR b.content LIKE %:keyword%) AND b.enabled = :enabled) OR " +
	           "  ((:searchType IS NULL OR :searchType = '') AND (b.title LIKE %:keyword% OR b.content LIKE %:keyword%) AND b.enabled = :enabled) " +
	           ")")
	    Page<Banner> searchByConditionFilter(@Param("searchType") String searchType, 
	                                         @Param("keyword") String keyword, 
	                                         @Param("enabled") Integer enabled, 
	                                         Pageable pageable);

	@Query("SELECT b FROM Banner b WHERE b.enabled = :enabled")
	Page<Banner> findAllFilter(Pageable pageable, @Param("enabled") Integer enabled);
}