package cmf.commitField.domain.user.controller;

import cmf.commitField.domain.user.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/match")
public class MatchController {
    private final MatchService matchService;

    @PostMapping("/request")
    public ResponseEntity<String> requestMatch(@RequestParam String userId, @RequestParam int duration) {
        matchService.enqueueUser(userId, duration);
        return ResponseEntity.ok("매칭 대기열에 추가됨");
    }
}
