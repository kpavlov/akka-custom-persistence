package example

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.cluster.sharding.{ClusterSharding, ClusterShardingSettings, ShardRegion}
import example.api._

object ExampleApp extends App {

  val system = ActorSystem("ClusterSystem")

  val extractEntityId: ShardRegion.ExtractEntityId = {
    case c: Command => (c.getId, c)
    case e: Event => (e.getId, e)
  }

  val numberOfShards = 100

  val extractShardId: ShardRegion.ExtractShardId = {
    case c: Command ⇒ (c.getId.hashCode % numberOfShards).toString
    case e: Event => (e.getId.hashCode % numberOfShards).toString
  }

  val workerRegion: ActorRef = ClusterSharding(system).start(
    typeName = "Workers",
    entityProps = Props[WorkerActor],
    settings = ClusterShardingSettings(system),
    extractEntityId = extractEntityId,
    extractShardId = extractShardId,
  )

  WorkerService.getInstance().registerShard(system.actorSelection(workerRegion.path))

  workerRegion ! new WorkCmd("3")

  workerRegion ! new CreateCmd("1")
  Thread.sleep(1000)
  workerRegion ! new WorkCmd("2")
  Thread.sleep(1000)
  workerRegion ! new SleepCmd("2")
  Thread.sleep(1000)
  workerRegion ! new WorkCmd("1")
  Thread.sleep(1000)
  workerRegion ! new WorkCmd("2")

  Thread.sleep(6000)

  workerRegion ! new SleepCmd("1")

  Thread.sleep(15000)

  system.terminate()
}
