package cmf.commitField.global.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.messaging.handler.HandlerMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            LoginCheck loginCheck = handlerMethod.getMethodAnnotation(LoginCheck.class);

            if (loginCheck != null) {
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    response.sendRedirect("/login");
                    return false;
                }
            }
        }
        return true;
    }
}
