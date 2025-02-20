package cmf.commitField;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CommitFieldApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommitFieldApplication.class, args);
	}

}
