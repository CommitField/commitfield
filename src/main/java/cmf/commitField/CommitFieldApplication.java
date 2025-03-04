package cmf.commitField;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
// 스케쥴링 활성화
// 테스트시에만 주석 풀기
@EnableScheduling
public class CommitFieldApplication {
	public static void main(String[] args) {
		SpringApplication.run(CommitFieldApplication.class, args);
	}

}
