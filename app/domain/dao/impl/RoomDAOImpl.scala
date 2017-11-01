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

  //TODO: ID값이 마지막 객체의 ID값 +1이 되는게 아니라 중간에 빈 값에 들어가게
  override def create(entity: Room): Future[Int] = {

    val rooms = TableQuery[Rooms]

    println(s"entity : $entity")

    val setup = rooms returning rooms.map(_.id) += entity

    val roomId = db.run(setup)

    roomId
  }

  override def get(id: Int) = ???

  override def update(entity: Room) = ???

  override def delete(id: Int) = ???
}