package cmf.commitField.domain.user.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final OAuth2User oauth2User;
    private final User user;

    public CustomOAuth2User(OAuth2User oauth2User, User user) {
        this.oauth2User = oauth2User;
        this.user = user;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }

    //TODO: 관리자 권한 추가시 해당 기능 추가 처리 필요, 당장은 관리자 권한 필요 없음.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    @Override
    public String getName() {
        return user.getUsername();
    }

    // Add the getId() method
    public Long getId() {
        return user.getId();  // Assuming that 'User' class has an 'id' field
    }
}