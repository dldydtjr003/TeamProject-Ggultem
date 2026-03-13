package com.honey.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.honey.domain.BusinessMember;

public interface BusinessMemberRepository extends JpaRepository<BusinessMember, Long> {

}
