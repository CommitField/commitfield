package cmf.commitField.domain.user.controller;

import cmf.commitField.domain.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class LogoutController {

    private final UserService userService;

    @PostMapping("/api/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = (String) request.getSession().getAttribute("username");
        // 세션 만료
        request.getSession().invalidate();
        System.out.println("로그아웃 성공!");

        // Update user status to false (로그아웃)
        if (username != null) {
            userService.updateUserStatus(username, false);
        } else {
            System.out.println("Username is null, cannot update status");
        }

        // 세션 쿠키 삭제
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setPath("/");  // 기본 경로 설정
        cookie.setMaxAge(0);  // 쿠키 만료 시간 설정
        response.addCookie(cookie);
        // CORS 대응을 위해 상태 코드만 반환하고, 프론트에서 리디렉션 처리하도록 함
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
