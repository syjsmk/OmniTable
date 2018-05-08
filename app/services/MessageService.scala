package services

import javax.inject.Inject

import domain.dao.MessageDAO
import domain.model.Message
import play.api.libs.json.JsObject

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class MessageService @Inject()(messageDAO: MessageDAO)(implicit executionContext: ExecutionContext) {

  def saveMessage(message: JsObject): Unit = {
    println("saveMessage")

    messageDAO.insert(Message(
      0,
      (message \ "time").as[String],
      (message \ "sender").as[String],
      (message \ "message").as[String],
      (message \ "roomId").as[Int]
    ))
  }

  def getMessages(): Future[Seq[Message]] = {
    messageDAO.getAll()
  }

  def getMessages(roomId: Int): Future[Seq[Message]] = {
    messageDAO.getAll().map(seq => seq.filter(message => message.roomId == roomId))
  }

}
