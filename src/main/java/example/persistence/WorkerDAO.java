package example.persistence;

import example.WorkerDetails;
import example.api.WorkerUpdate;
import org.slf4j.Logger;
import scala.Option;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Simple in-memory implementation of synchronous DAO
 */
public class WorkerDAO {

    private final Logger logger = getLogger(WorkerDAO.class);
    private final Map<String, WorkerDetails> stateByIdMap = new ConcurrentHashMap<>();

    private static final WorkerDAO INSTANCE = new WorkerDAO();

    public static WorkerDAO getInstance() {
        return INSTANCE;
    }

    public WorkerDAO() {
        init();
    }

    private void init() {
        final String id = "2";
        final WorkerDetails details = new WorkerDetails(id, 50, 13);
        stateByIdMap.put(id, details);
    }

    public WorkerDetails get(String workerId) {
        final WorkerDetails details = stateByIdMap.get(workerId);
        logger.info("Loaded Details by ID {} => {}", workerId, details);
        return details;
    }

    public void writeJournal(String workerId, WorkerUpdate update) {
        logger.info("Writing to Journal ID {} => {}", workerId, update);
    }
    public void save(String workerId, WorkerDetails details) {
        stateByIdMap.put(workerId, details);
        logger.info("Saved Details by ID {} => {}", workerId, details);
    }
}
