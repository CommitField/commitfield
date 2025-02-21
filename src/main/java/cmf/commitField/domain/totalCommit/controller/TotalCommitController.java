package cmf.commitField.domain.totalCommit.controller;

import cmf.commitField.domain.totalCommit.dto.TotalCommitResponseDto;
import cmf.commitField.domain.totalCommit.service.TotalCommitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TotalCommitController {
    private final TotalCommitService totalCommitService;

    @GetMapping("/api/commits/{username}")
    public TotalCommitResponseDto getTotalCommits(@PathVariable String username) {
        return totalCommitService.getTotalCommitCount(username);
    }
}
