package example

case class WorkerDetails(id: String, health: Int, mana: Int) {

  override def toString: String = s"{id: ${id}, health: ${health}, mana: ${mana}}"
}
