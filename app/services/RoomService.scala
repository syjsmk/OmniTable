package services

import javax.inject.Inject

import akka.actor.FSM.Failure
import akka.actor.Status.Success
import domain.dao.RoomDAO
import domain.dao.impl.RoomDAOImpl
import domain.model.Room

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

// RoomDAO가 아니라 RoomDAOImpl을 DI 하게 되면 그게 의미가 있나?
class RoomService @Inject()(roomDao: RoomDAOImpl) {

  def getRoom(id: Int): Future[Option[Room]] = {

    roomDao.get(id)

  }


  def makeRoom(name: String): Future[Int] = {

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

  def updateRoom(id: Int, name: String): Future[Option[Room]] = {

    this.updateRoom(Room(id, name))
  }

  def deleteRoom(id: Int): Future[Int] = {

   roomDao.delete(id)

  }

}
