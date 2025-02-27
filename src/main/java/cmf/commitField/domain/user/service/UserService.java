package cmf.commitField.domain.user.service;

import cmf.commitField.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void updateUserStatus(String username, boolean status) {
        System.out.println("Updating status for " + username + " to " + status);
        userRepository.updateStatus(username, status);
    }
}
