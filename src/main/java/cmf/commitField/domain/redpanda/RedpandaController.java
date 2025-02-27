package cmf.commitField.domain.redpanda;

import cmf.commitField.domain.commit.scheduler.CommitScheduler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kafka")
public class RedpandaController {

    private final RedpandaProducer redpandaProducer;
    private final CommitScheduler commitScheduler;

    public RedpandaController(RedpandaProducer redpandaProducer, CommitScheduler commitScheduler) {
        this.redpandaProducer = redpandaProducer;
        this.commitScheduler = commitScheduler;
    }

}