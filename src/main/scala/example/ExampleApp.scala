package example

import akka.actor.{ActorRef, ActorSystem, PoisonPill, Props}
import akka.cluster.sharding.{ClusterSharding, ClusterShardingSettings, ShardRegion}
import example.api._

object ExampleApp extends App {

  val system = ActorSystem("ClusterSystem")

  val extractEntityId: ShardRegion.ExtractEntityId = {
    case c: Command  => (c.getId, c)
    case e: Event => (e.getId, e)
//        case _              => println(s"Unexpected message ${}")
  }

  val numberOfShards = 100

  val extractShardId: ShardRegion.ExtractShardId = {
    case c: Command ⇒ (c.getId.hashCode % numberOfShards).toString
    case e: Event => (e.getId.hashCode  % numberOfShards).toString
  }

  val workerRegion: ActorRef = ClusterSharding(system).start(
    typeName = "Workers",
    entityProps = Props[WorkerActor],
    settings = ClusterShardingSettings(system),
    extractEntityId = extractEntityId,
    extractShardId = extractShardId)

//  val worker = system.actorSelection("worker-1")

//  printf(s"Actor ref ${worker}")

//  val locator = system.actorOf(Props[WorkerLocator], "workers")

//  workerRegion ! WorkCmd("1")

//  locator ! Lookup("2")

  WorkerService.getInstance().registerShard(system.actorSelection(workerRegion.path))
//  WorkerService.getInstance().registerShard(workerRegion)

  workerRegion ! new WorkCmd("3")

  workerRegion ! new CreateCmd("1")
//
  workerRegion ! new WorkCmd("2")
//
  workerRegion ! new SleepCmd("2")
  workerRegion ! new WorkCmd("1")
  workerRegion ! new WorkCmd("2")


  Thread.sleep(6000)
  
  workerRegion ! new SleepCmd("1")

  Thread.sleep(500000)

  system.terminate()



}
