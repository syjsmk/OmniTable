package services

import javax.inject.Inject

import domain.dao.RoomDAO
import domain.model.Room

import scala.concurrent.Future

class RoomService @Inject()(roomDao: RoomDAO) {

  def getRoom(id: Int): Future[Option[Room]] = {

    roomDao.get(id)

  }

  def makeRoom(name: String, userCount: Int): Future[Int] = {

    val roomId = roomDao.create(Room(0, name))

    roomId
  }


  def getRooms(): Future[Seq[Room]] = {
    roomDao.getAll()
  }

  def updateRoom(entity: Room): Future[Option[Room]] = {

    val room = roomDao.update(entity)

    room
  }

  //  def updateRoom(id: Int, name: String, userCount: Int): Future[Option[Room]] = {
  //
  //    this.updateRoom(Room(id, name, userCount))
  //  }
  def updateRoom(id: Int, attrName: String, attrValue: Any): Future[Option[Room]] = {

    roomDao.update(id, attrName, attrValue)
  }

  def deleteRoom(id: Int): Future[Int] = {

    roomDao.delete(id)

  }

}
