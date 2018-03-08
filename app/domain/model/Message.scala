package domain.model

// 이거 import 안해주면 아래 extends Table[] 등 대부분이 동작 안함
import slick.jdbc.H2Profile.api._
import play.api.libs.json.Json



case class Message(id: Int, time: String, sender: String, message: String)

// Message별로 id를 가질 필요가 있나?
class Messages(tag: Tag) extends Table[Message](tag, "message") {

//  val rooms = TableQuery[Rooms]

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def time = column[String]("time")
  def sender = column[String]("sender")
  def message = column[String]("message")

  def * = (id, time, sender, message) <> ((Message.apply _).tupled, Message.unapply)
}

