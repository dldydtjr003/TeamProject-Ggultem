package com.honey.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.gson.Gson;
import com.honey.dto.MemberDTO;
import com.honey.util.JWTUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class JWTCheckFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		log.info("--------------------  JWTCheckFilter ------------------------------------------------------ ");
		String authHeaderStr = request.getHeader("Authorization");
		// ✅ 토큰 없으면 그냥 통과 (인증 없이 접근 가능한 API용)
	    if (authHeaderStr == null || !authHeaderStr.startsWith("Bearer ")) {
	        filterChain.doFilter(request, response);
	        return;
	    }
		try {
			// Bearer accestoken ............... 토큰이 정상적이면 그대로 요구사항진행
			// Bearer[공백]accestoken... "Bearer " 접두사를 제외한 JWT 토큰만 추출
			String accessToken = authHeaderStr.substring(7);
			Map<String, Object> claims = JWTUtil.validateToken(accessToken);
			log.info("JWT claims: " + claims);

			// filterChain.doFilter(request, response); //이하 추가
			String email = (String) claims.get("email");
			String pw = (String) claims.get("pw");
			String nickname = (String) claims.get("nickname");
			Boolean social = (Boolean) claims.get("social");
			Set<String> roleNames = (Set<String>) claims.get("roleNames");
			LocalDateTime regDate = (LocalDateTime) claims.get("regDate");

			MemberDTO memberDTO = new MemberDTO(email, pw, nickname, social.booleanValue(), roleNames, regDate);

			// 스프링 시큐리티에서 인증 정보를 담는 객체
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberDTO,
					pw, memberDTO.getAuthorities());

			// 이 객체를 SecurityContextHolder에 넣으면,
			// 해당 요청은 인증된 사용자로 처리됨
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);

			filterChain.doFilter(request, response);

		} catch (Exception e) {
			log.error("JWT Check Error .................................... ");
			log.error(e.getMessage());
			Gson gson = new Gson();
			String msg = gson.toJson(Map.of("error", "ERROR_ACCESS_TOKEN"));
			response.setContentType("application/json");
			PrintWriter printWriter = response.getWriter();
			printWriter.println(msg);
			printWriter.close();
		}
	}
	/*
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
	    String path = request.getRequestURI();
	    String method = request.getMethod();

	    // 1. OPTIONS 요청은 브라우저의 사전 확인용이므로 무조건 통과
	    if (method.equals("OPTIONS")) return true;

	    // 2. 대소문자 무시하고 '로그인 안 해도 되는' 경로들 🧤
	    String lowercasePath = path.toLowerCase();
	    
	    if (lowercasePath.startsWith("/login") || 
	        lowercasePath.startsWith("/api/member/login") ||
	        lowercasePath.startsWith("/member/kakao") ||
	        lowercasePath.startsWith("/admin/member/") || // 중복확인, 비번재설정 한방에 해결!
	        lowercasePath.startsWith("/api/mail/") ||
	        lowercasePath.startsWith("/member/refresh") ||
	        lowercasePath.startsWith("/mypage/") ||
	        lowercasePath.startsWith("/board/") ||
	        lowercasePath.startsWith("/itemboard/") ||
	        lowercasePath.startsWith("/ws") ||
	        path.equals("/") ) {
	        return true;
	    }

	    return false;
	}
	*/
	//배포 후 기능 테스트를 위한 전체 예외처리
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
	    return true; 
	}

}
