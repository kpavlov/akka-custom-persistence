package example

import akka.actor.{Actor, ActorIdentity, Identify}


case class Lookup(id: String) {
}

class WorkerLocator extends Actor {
  

  override def receive: Receive = {
    case Lookup(actorId) => {
      println(s"Requested actor with identity ${actorId}")
      context.actorSelection("/user/worker-" + actorId) ! Identify(None)
    }
    case ActorIdentity(correlationId, Some(ref)) => {
      println(s"Got actor identity ${ref}")
    }
    case ActorIdentity(correlationId, None) => {
      println(s"No actor identity ${correlationId}")
    }
  }
}
