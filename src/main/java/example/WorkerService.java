package example;

import akka.actor.ActorSelection;
import example.api.Event;
import example.api.WorkerUpdate;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

public class WorkerService {
    private final Logger logger = getLogger(WorkerService.class);
    private static WorkerService ourInstance = new WorkerService();
    private volatile ActorSelection shardRegionSelection;

    public static WorkerService getInstance() {
        return ourInstance;
    }

    private WorkerService() {
    }

    public void work(WorkerDetails details) {
        final String id = details.id();
        logger.info("Working action {}", id);
        shardRegionSelection.tell(new Event(id, new WorkerUpdate(-5, 10)), null);
    }

    public void sleep(WorkerDetails details) {
        final String id = details.id();
        logger.info("Sleeping action {}", id);
        shardRegionSelection.tell(new Event(id, new WorkerUpdate(10, -3)), null);

    }

    public void registerShard(ActorSelection actorSelection) {
        this.shardRegionSelection = actorSelection;
        logger.info("Registered shard region selector: {}", actorSelection);
    }
}
