package example

import akka.actor.{ActorLogging, PoisonPill, ReceiveTimeout}
import akka.persistence.{PersistentActor, RecoveryCompleted, SnapshotOffer}
import example.api._

import scala.concurrent.duration._


object WorkerState {
  //  val health:Int
}


class WorkerActor extends PersistentActor with ActorLogging {

  // passivate the entity when no activity
  context.setReceiveTimeout(2 seconds)

  override def persistenceId: String = {
    //    log.info("Extracting id from {}", self.path.name)
    self.path.name
  }

  var state: WorkerDetails = _

  def onEvent(evt: Event): Unit = {
    log.info(s"Update state from event: ${evt}")
    persist(evt) { evt =>
      updateState(evt.getUpdate)
    }
  }

  private def updateState(update: WorkerUpdate) {
    state = WorkerDetails(state.id,
      state.health + update.getHealthDelta(),
      state.mana + update.getManaDelta
    )
    log.info("New State: {}", state)
  }

  def setState(snapshot: WorkerDetails): Unit = {
    this.state = snapshot
    log.info("State Set: {}", state)
  }

  override def receiveRecover: Receive = {
    case SnapshotOffer(_, snapshot: WorkerDetails) => {
      setState(snapshot)
    }
    case RecoveryCompleted => {
      if (state != null) {
        context.become(persistentState)
      } 
    }
  }

  override def postStop(): Unit = {
    log.info("Worker stopped: {}", persistenceId)
  }


  override protected def onRecoveryFailure(cause: Throwable, event: Option[Any]): Unit = {
    log.info("Recovery failed")
    super.onRecoveryFailure(cause, event)
  }

  def transientState: Receive = {
    case cmd: CreateCmd => {
      log.info("Created: {}", cmd.getId)
      this.state = WorkerDetails(cmd.getId, 100, 1)
      context.become(persistentState)
    }
    case _ => killSelf("Worker {} NOT FOUND. Killing Actor")
  }

  def persistentState: Receive = {
    case cmd: WorkCmd => {
      log.info("Working {}", persistenceId)
      WorkerService.getInstance().work(state)
    }
    case cmd: SleepCmd => {
      log.info("Sleeping {}", persistenceId)
      WorkerService.getInstance().sleep(state)
    }
    case evt: Event => onEvent(evt)
    case ReceiveTimeout => killSelf("Killing Worker {} by Timeout to free-up some RAM")
  }

  def killSelf(reason: String) = {
    log.info(reason, persistenceId)
    context.setReceiveTimeout(Duration.Undefined)
    self ! PoisonPill
  }

  override def receiveCommand = transientState;

  override def journalPluginId: String = "worker-journal"

  override def snapshotPluginId: String = "worker-snapshot-store"
}
