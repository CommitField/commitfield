package cmf.commitField.domain.user.service;

import cmf.commitField.domain.user.entity.CustomOAuth2User;
import cmf.commitField.domain.user.entity.User;
import cmf.commitField.domain.user.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oauthUser = super.loadUser(userRequest);

        Map<String, Object> attributes = oauthUser.getAttributes();
        String username = (String) attributes.get("login");  // GitHub ID
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String avatarUrl = (String) attributes.get("avatar_url");

        // 이메일이 없는 경우를 대비하여 기본값 설정
        if (email == null) {
            email = username + "@github.com";
        }

        Optional<User> existingUser = userRepository.findByUsername(username);
        User user;
        if (existingUser.isPresent()) {
            user = existingUser.get();
            user.setAvatarUrl(avatarUrl);
            user.setEmail(email);  // GitHub에서 이메일이 변경될 수도 있으니 업데이트\
        } else {
            user = new User(username, email, name, avatarUrl, User.Role.USER, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
            userRepository.save(user);
        }

        return new CustomOAuth2User(oauthUser, user);
    }

    // id로 user 조회
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }
}
