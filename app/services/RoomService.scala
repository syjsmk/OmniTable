package services

import javax.inject.Inject

import akka.actor.FSM.Failure
import akka.actor.Status.Success
import domain.dao.RoomDAO
import domain.dao.impl.RoomDAOImpl
import domain.model.Room

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

// RoomDAO가 아니라 RoomDAOImpl을 DI 하게 되면 그게 의미가 있나?
class RoomService @Inject()(roomDao: RoomDAOImpl) {

  def makeRoom(name: String): Int = {

    println("RoomService")
    println(s"roomName: $name")

    val room = Room(1, name)
    val roomId = roomDao.create(room)

    // 여기에 DB 입출력?


    var result: Int = 9

    println(Await.result(roomId, 10 second))


    result
  }


  def getRooms() = {

  }

}
