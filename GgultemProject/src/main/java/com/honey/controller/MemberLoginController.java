package com.honey.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.honey.dto.MemberDTO;
import com.honey.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberLoginController {
	
	private final MemberService memberService;

    @GetMapping("/kakao")
    public MemberDTO getMemberFromKakao(String code) {
        log.info("카카오 인가 코드: " + code);

        // 1. 코드를 이용해 카카오 사용자 정보 가져오기
        // 2. 우리 시스템의 JWT 토큰 발행하기
        // (이 로직은 Service에서 구현할 거예요)
        return memberService.getKakaoMember(code);
    }
    
    @PostMapping("/google/accessToken")
    public Map<String, Object> googleLogin(@RequestBody Map<String, String> params) {
    	String authCode = params.get("code");
        // 서비스에서 구글 사용자 정보 처리 및 JWT 발급까지 한 번에!
        MemberDTO memberDTO = memberService.getMemberWithGoogle(authCode);

        return memberDTO.getClaims();
    }
}
