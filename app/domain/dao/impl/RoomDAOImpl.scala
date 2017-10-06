package domain.dao.impl

import domain.dao.RoomDAO
import domain.model.Room
import play.api.libs.json.Json

class RoomDAOImpl extends RoomDAO {

  override def getAll(): Option[Seq[Room]] = {

    val r1: Room = new Room()
    r1.id = 1
    r1.name = "first"
    val r2: Room = new Room()
    r2.id = 2
    r2.name = "second"

    Some(Seq(r1, r2))
  }

  override def create(entity: Room): Option[Room] = ???

  override def findById(entity: Room): Option[Room] = ???

  override def get(id: Int): Room = ???

}