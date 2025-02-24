package cmf.commitField.global.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoginCheckInterceptor loginCheckInterceptor;

    public WebConfig(LoginCheckInterceptor loginCheckInterceptor) {
        this.loginCheckInterceptor = loginCheckInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // @LoginCheck가 붙은 메서드에 대해 인터셉터를 적용
        registry.addInterceptor(loginCheckInterceptor)
                .addPathPatterns("/chat/**");  // /chat 경로에만 인터셉터 적용
    }
}