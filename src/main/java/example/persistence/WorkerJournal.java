package example.persistence;


import akka.dispatch.Futures;
import akka.persistence.AtomicWrite;
import akka.persistence.PersistentRepr;
import akka.persistence.journal.japi.AsyncWriteJournal;
import example.api.Event;
import org.slf4j.Logger;
import scala.collection.immutable.Seq;
import scala.concurrent.Future;

import java.util.Collections;
import java.util.Optional;
import java.util.function.Consumer;

import static org.slf4j.LoggerFactory.getLogger;

class WorkerJournal extends AsyncWriteJournal {

    private final Logger logger = getLogger(WorkerJournal.class);
    private final WorkerDAO dao;

    public WorkerJournal() {
        dao = WorkerDAO.getInstance();
    }

    @Override
    public Future<Void> doAsyncReplayMessages(String persistenceId, long fromSequenceNr, long toSequenceNr, long max, Consumer<PersistentRepr> replayCallback) {
        throw new UnsupportedOperationException("Method is not implemented: example.persistence.WorkerJournal.doAsyncReplayMessages");
    }

    @Override
    public Future<Long> doAsyncReadHighestSequenceNr(String persistenceId, long fromSequenceNr) {
        return Futures.successful(0L);
    }

    @Override
    public Future<Iterable<Optional<Exception>>> doAsyncWriteMessages(Iterable<AtomicWrite> messages) {

        logger.info("Writing: {}", messages);
        for (AtomicWrite message : messages) {
            final Seq<PersistentRepr> payload = message.payload();
            payload.foreach(p -> {
                Event e = (Event) p.payload();
                logger.info("Writing Event: {}", e);
                dao.writeJournal(e.getId(), e.getUpdate());

                return null;
            });
        }
        return Futures.successful(Collections.emptyList());
    }

    @Override
    public Future<Void> doAsyncDeleteMessagesTo(String persistenceId, long toSequenceNr) {
        throw new UnsupportedOperationException("Method is not implemented: example.persistence.WorkerJournal.doAsyncDeleteMessagesTo");
    }
}
