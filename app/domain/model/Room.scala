package domain.model

import slick.driver.H2Driver.api._
import play.api.libs.json._

//class Room {
//
//  var id: Int = _
//  var name: String = _
//
//}

//case class Room(id: Int, name: String, genre: String, description: String, userCount: Int, tag: List[String])

case class Room(id: Int, name: String, userCount: Int)

class Rooms(tag: Tag) extends Table[Room](tag, "room") {
  def id = column[Int]("id", O.PrimaryKey)
  def name = column[String]("name")
  def userCount = column[Int]("userCount")

  def * = (id, name, userCount) <> ((Room.apply _).tupled, Room.unapply)
}

object Formatters {
  implicit val roomFormat = Json.format[Room]
}