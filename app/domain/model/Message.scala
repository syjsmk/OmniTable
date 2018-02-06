package domain.model

// 이거 import 안해주면 아래 extends Table[] 등 대부분이 동작 안함
import slick.jdbc.H2Profile.api._

import play.api.libs.json.Json


case class Message(roomId: Int, time: String, sender: String, message: String)

class Messages(tag: Tag) extends Table[Message](tag, "message") {
  def roomId = column[Int]("roomId", O.PrimaryKey)
  def time = column[String]("time")
  def sender = column[String]("sender")
  def message = column[String]("message")

  def * = (roomId, time, sender, message) <> ((Message.apply _).tupled, Message.unapply)
}