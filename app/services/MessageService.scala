package services

import javax.inject.Inject

import domain.dao.MessageDAO
import domain.model.Message
import play.api.libs.json.JsObject

import scala.concurrent.ExecutionContext

class MessageService @Inject()(messageDAO: MessageDAO) {

  def saveMessage(message: JsObject): Unit = {
    println("saveMessage")

    messageDAO.insert(Message(
      0,
      (message \ "time").as[String],
      (message \ "sender").as[String],
      (message \ "message").as[String]
    ))
  }

}
