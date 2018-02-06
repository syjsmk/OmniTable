package domain.model

import slick.jdbc.H2Profile.api._
import play.api.libs.json._

//class Room {
//
//  var id: Int = _
//  var name: String = _
//
//}

//case class Room(id: Int, name: String, genre: String, description: String, userCount: Int, tag: List[String])

case class Room(id: Int, name: String)

class Rooms(tag: Tag) extends Table[Room](tag, "room") {
  def id = column[Int]("id", O.PrimaryKey)
  def name = column[String]("name")

  def * = (id, name) <> ((Room.apply _).tupled, Room.unapply)
}

object Formatters {
  implicit val roomFormat = Json.format[Room]
  implicit val messageFormat = Json.format[Message]
}