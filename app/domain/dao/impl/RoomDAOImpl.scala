package domain.dao.impl


import javax.inject.{Inject, Singleton}

import domain.dao.RoomDAO
import domain.model.{Room, Rooms}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._

class RoomDAOImpl @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends RoomDAO {

  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  val rooms = TableQuery[Rooms]

  override def getAll(): Future[Seq[Room]] = {

    db.run(rooms.result)
  }

  override def create(entity: Room): Future[Int] = {

    val rooms = TableQuery[Rooms]

    var prevRoomId = 0
    var id = 0

    db.run(rooms.result).map(seq => {

      seq.foreach(room => {
        if(room.id - prevRoomId > 1) {
          id = prevRoomId + 1
        } else {
          id = room.id + 1
        }

        prevRoomId = room.id
      })
      
      db.run(rooms += Room(id, entity.name))

      id
    })

  }

  override def get(id: Int) = ???

  override def update(entity: Room) = ???

  override def delete(id: Int) = ???
}