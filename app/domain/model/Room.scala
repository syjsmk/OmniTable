package domain.model

import slick.driver.H2Driver.api._

//class Room {
//
//  var id: Int = _
//  var name: String = _
//
//}

case class Room(id: Int, name: String)

class Rooms(tag: Tag) extends Table[Room](tag, "room") {
  def id = column[Int]("id", O.PrimaryKey)
  def name = column[String]("name")

  def * = (id, name) <> ((Room.apply _).tupled, Room.unapply)
}