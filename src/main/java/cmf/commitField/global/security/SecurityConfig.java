package cmf.commitField.global.security;

import cmf.commitField.domain.user.service.CustomOAuth2UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private StringRedisTemplate redisTemplate;
    private final CustomOAuth2UserService customOAuth2UserService;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService) {
        this.customOAuth2UserService = customOAuth2UserService;
    }

    @Bean
    protected SecurityFilterChain config(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("**").permitAll()  // TODO: 차후 인증 관련 추가 필요 
                )

                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 설정 추가
                .csrf(AbstractHttpConfigurer::disable) // CSRF 보호 비활성화
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)  // 세션 정책 설정
                        .invalidSessionUrl("/login?error=invalidSession")  // 세션이 유효하지 않으면 이동할 URL
                        .maximumSessions(1)  // 하나의 계정으로 한 번에 로그인할 수 있도록 제한
                        .expiredUrl("/login?error=sessionExpired")  // 세션 만료 후 이동할 URL 설정
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")  // 로그인 페이지 지정
                        .successHandler((request, response, authentication) -> {
                            SecurityContextHolder.getContext().setAuthentication(authentication);

                            OAuth2User principal = (OAuth2User) authentication.getPrincipal();
                            String username = principal.getAttribute("login");

                            // Redis에 유저 활성화 정보 저장
                            customOAuth2UserService.setUserActive(username);

                            // 디버깅 로그
                            System.out.println("OAuth2 로그인 성공: " + username);
                            response.sendRedirect("http://localhost:5173/home");  // 로그인 성공 후 리다이렉트
                        })
                )
                .logout(logout -> logout
                        .logoutUrl("api/logout")  // 로그아웃 URL 설정
                        .invalidateHttpSession(true)  // 로그아웃 시 세션 무효화
                        .clearAuthentication(true)  // 인증 정보 지우기
                        .deleteCookies("JSESSIONID")  // 세션 쿠키 삭제
                        .logoutSuccessHandler((request, response, authentication) -> {
                            OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
                            OAuth2User principal = oauth2Token.getPrincipal();
                            String username = principal.getAttribute("login");

                            //레디스에서 정보 삭제
                            removeUserActive(username);

                            System.out.println("로그아웃 성공");
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.sendRedirect("http://localhost:5173/"); // 로그아웃 후 홈으로 이동
                        })
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);

        // setAllowedOrigins 대신 setAllowedOriginPatterns 사용
        config.setAllowedOrigins(List.of("http://localhost:5173/"));

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    public void removeUserActive(String username) {
        redisTemplate.delete("commit_active:" + username);
        redisTemplate.delete("commit_lastCommitted:" + username);
    }
}
