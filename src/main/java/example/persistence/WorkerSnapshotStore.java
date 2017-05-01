package example.persistence ;

//import akka.persistence.snapshot.SnapshotStore
//import akka.persistence.{SelectedSnapshot, SnapshotMetadata, SnapshotSelectionCriteria}

//import scala.concurrent.ExecutionContext
//import scala.concurrent.ExecutionContext.implicits.

//import scala.concurrent.Future


import akka.dispatch.Futures;
import akka.persistence.SelectedSnapshot;
import akka.persistence.SnapshotMetadata;
import akka.persistence.SnapshotSelectionCriteria;
import akka.persistence.snapshot.japi.SnapshotStore;
import example.WorkerDetails;
import scala.concurrent.Future;

import java.util.Optional;

class WorkerSnapshotStore extends SnapshotStore {



  @Override
  public Future<Optional<SelectedSnapshot>> doLoadAsync(String persistenceId, SnapshotSelectionCriteria criteria) {
    final WorkerDetails workerDetails = WorkerDAO.getInstance().get(persistenceId);

    if (workerDetails == null) {
      return Futures.successful(Optional.empty());
    }

    final SelectedSnapshot selectedSnapshot = new SelectedSnapshot(
            new SnapshotMetadata(persistenceId, 0, 0L), workerDetails
    );

    return Futures.successful(Optional.of(selectedSnapshot));
  }

  @Override
  public Future<Void> doSaveAsync(SnapshotMetadata metadata, Object snapshot) {
    throw new UnsupportedOperationException("Method is not implemented: example.persistence.WorkerSnapshotStore.doSaveAsync");
  }

  @Override
  public Future<Void> doDeleteAsync(SnapshotMetadata metadata) {
    throw new UnsupportedOperationException("Method is not implemented: example.persistence.WorkerSnapshotStore.doDeleteAsync");
  }

  @Override
  public Future<Void> doDeleteAsync(String persistenceId, SnapshotSelectionCriteria criteria) {
    throw new UnsupportedOperationException("Method is not implemented: example.persistence.WorkerSnapshotStore.doDeleteAsync");
  }

//  private val dao:WorkerDAO = WorkerDAO.getInstance()
//
//  override def loadAsync(persistenceId: String, criteria: SnapshotSelectionCriteria, implicit ec: ExecutionContext): Future[Option[SelectedSnapshot]] = Future[Option[SelectedSnapshot]] {
//    val details = dao.get(persistenceId)
//    if (details == null) {
//      Option.empty
//    } else {
//      Option.apply(SelectedSnapshot(SnapshotMetadata(persistenceId, 0, 0L), details))
//    }
//  }
//
//  override def saveAsync(metadata: SnapshotMetadata, snapshot: Any): Future[Unit] = ???
//
//  override def deleteAsync(metadata: SnapshotMetadata): Future[Unit] = ???
//
//  override def deleteAsync(persistenceId: String, criteria: SnapshotSelectionCriteria): Future[Unit] = ???
}
