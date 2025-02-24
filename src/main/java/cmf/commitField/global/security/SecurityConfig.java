package cmf.commitField.global.security;

import cmf.commitField.domain.user.entity.CustomOAuth2User;
import cmf.commitField.domain.user.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService) {
        this.customOAuth2UserService = customOAuth2UserService;
    }

    @Bean
    protected SecurityFilterChain config(HttpSecurity http) throws Exception {
        // 권한 설정
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/**").permitAll() // actuator 엔드포인트 허용
                        .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
                );

        //로그인 관련 설정
        http
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")  // 로그인 페이지 지정
                        .successHandler((request, response, authentication) -> {
                            // 인증 정보가 SecurityContext에 추가되는 것을 보장
                            SecurityContextHolder.getContext().setAuthentication(authentication);

                            CustomOAuth2User customUser = (CustomOAuth2User) authentication.getPrincipal();

                            // 디버깅: authentication 정보 확인
                            System.out.println("Authentication: " + authentication);
                            System.out.println("Principal: " + authentication.getPrincipal());

                            if (authentication != null && authentication.getPrincipal() != null) {
                                //인가가 있으면 유저 정보를 저장
                                OAuth2User principal = (OAuth2User) authentication.getPrincipal();
                                String username = principal.getAttribute("login");

                                // 세션에 사용자 정보를 추가
                                request.getSession().setAttribute("user", username);

                                response.sendRedirect("/");  // 로그인 성공 후 리다이렉트
                            } else {
                                // 인증 실패 시 처리
                                response.sendRedirect("/login?error=authenticationFailed");
                            }
                        })
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)  // 세션 정책 설정
                        .invalidSessionUrl("/login?error=invalidSession")  // 세션이 유효하지 않으면 이동할 URL
                        .maximumSessions(1)  // 하나의 계정으로 한 번에 로그인할 수 있도록 제한
                        .expiredUrl("/login?error=sessionExpired")  // 세션 만료 후 이동할 URL 설정
                );

        //로그아웃 관련 설정
        http
                .logout(logout -> logout
                        .logoutUrl("/logout")  // 로그아웃 URL 설정
                        .logoutSuccessUrl("/")  // 로그아웃 성공 후 이동할 URL
                        .invalidateHttpSession(true)  // 로그아웃 시 세션 무효화
                        .clearAuthentication(true)  // 인증 정보 지우기
                        .deleteCookies("JSESSIONID")  // 세션 쿠키 삭제
                );
        http
                .csrf(
                        AbstractHttpConfigurer::disable  // CSRF 보호 비활성화
                );

        return http.build();
    }
}