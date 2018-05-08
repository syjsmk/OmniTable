package domain.model

// 이거 import 안해주면 아래 extends Table[] 등 대부분이 동작 안함
import slick.jdbc.H2Profile.api._
import play.api.libs.json.Json



case class Message(id: Int, time: String, sender: String, message: String, roomId: Int)

// Message별로 id를 가질 필요가 있나?
class Messages(tag: Tag) extends Table[Message](tag, "message") {

  val rooms = TableQuery[Rooms]

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def time = column[String]("time")
  def sender = column[String]("sender")
  def message = column[String]("message")
  def roomId = column[Int]("roomId")

  def * = (id, time, sender, message, roomId) <> ((Message.apply _).tupled, Message.unapply)

  def room = foreignKey("room", id, rooms)(_.id, onUpdate = ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)
}

