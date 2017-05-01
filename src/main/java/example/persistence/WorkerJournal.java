package example.persistence;


import akka.dispatch.Futures;
import akka.persistence.AtomicWrite;
import akka.persistence.PersistentRepr;
import akka.persistence.journal.japi.AsyncWriteJournal;
import org.slf4j.Logger;
import scala.concurrent.Future;

import java.util.Collections;
import java.util.Optional;
import java.util.function.Consumer;

import static org.slf4j.LoggerFactory.getLogger;

class WorkerJournal extends AsyncWriteJournal {

  private final Logger logger = getLogger(WorkerJournal.class);

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
                 return Futures.successful(Collections.emptyList());
//    throw new UnsupportedOperationException("Method is not implemented: example.persistence.WorkerJournal.doAsyncWriteMessages");
  }

  @Override
  public Future<Void> doAsyncDeleteMessagesTo(String persistenceId, long toSequenceNr) {
    throw new UnsupportedOperationException("Method is not implemented: example.persistence.WorkerJournal.doAsyncDeleteMessagesTo");
  }
}
