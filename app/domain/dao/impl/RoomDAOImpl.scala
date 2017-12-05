package domain.dao.impl


import javax.inject.{Inject, Singleton}

import domain.dao.RoomDAO
import domain.model.{Room, Rooms}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._

import scala.util.control.Breaks._



class RoomDAOImpl @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends RoomDAO {

  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  val rooms = TableQuery[Rooms]

  val ROOM_NAME = "name"
  val USER_COUNT = "userCount"

  override def getAll(): Future[Seq[Room]] = {

    db.run(rooms.result)
  }

  override def create(entity: Room): Future[Int] = {
    var prevRoomId = -1
    var id = -1

    db.run(rooms.result).map(seq => {

      breakable{
        for(room <- seq) {

          if(room.id - prevRoomId > 1) {
            id = prevRoomId + 1
            break
          } else {
            id = room.id + 1
          }

          prevRoomId = room.id
        }
      }

      db.run(rooms += Room(id, entity.name, entity.userCount))

      id
    })

  }

  override def get(id: Int): Future[Option[Room]] = {

    db.run(rooms.filter(_.id === id).result.headOption)

  }

  override def update(entity: Room): Future[Option[Room]] = {

    val q = for(room <- rooms if room.id === entity.id) yield room.name

    val updateAction = q.update(entity.name)
    db.run(updateAction)


    this.get(entity.id)
  }

  override def update(id: Int, attrName: String, attrValue: Any): Future[Option[Room]] = {

    attrName match {
      case ROOM_NAME => {
        val q = rooms.filter(_.id === id).map(room => {
          room.name
        }).update(attrValue.toString)
        db.run(q)
      }
      case USER_COUNT => {
        val q = rooms.filter(_.id === id).map(room => {
          room.userCount
        }).update(attrValue.toString.toInt)
        db.run(q)
      }
    }

    this.get(id)
  }

  override def delete(id: Int): Future[Int] = {
    db.run(rooms.filter(_.id === id).delete)
  }

}