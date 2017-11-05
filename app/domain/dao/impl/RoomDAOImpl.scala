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

  override def get(id: Int): Future[Option[Room]] = {

    db.run(rooms.filter(_.id === id).result.headOption)

  }

  override def update(entity: Room): Future[Option[Room]] = {

    println("update")

    val q = for(room <- rooms if room.id === entity.id) yield room.name

    println(s"q : $q")
    println(s"entity.name: $entity")
    val updateAction = q.update(entity.name)
    db.run(updateAction)


    this.get(entity.id)
  }

  override def delete(id: Int) = ???
}