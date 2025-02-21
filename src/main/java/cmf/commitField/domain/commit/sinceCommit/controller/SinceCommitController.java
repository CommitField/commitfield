package cmf.commitField.domain.commit.sinceCommit.controller;

import cmf.commitField.domain.commit.sinceCommit.dto.SinceCommitResponseDto;
import cmf.commitField.domain.commit.sinceCommit.service.SinceCommitService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class SinceCommitController {
    private final SinceCommitService sinceCommitService;

    @GetMapping("/api/github/commits-since")
    public ResponseEntity<List<SinceCommitResponseDto>> getCommits(
            @RequestParam String owner,
            @RequestParam String repo,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime since,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime until
            ) {
        List<SinceCommitResponseDto> sinceCommits = sinceCommitService.getSinceCommits(owner, repo, since, until);

        return ResponseEntity.ok(sinceCommits);
    }
}
