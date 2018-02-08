package services

import javax.inject.Inject

import domain.dao.MessageDAO
import play.api.libs.json.JsObject

/*
RoomService와 마찬가지로 MessageDAO로 Inject를 할 경우

1) No implementation for domain.dao.MessageDAO was bound.
라는 에러가 뜸
*/
class MessageService @Inject()(messageDAO: MessageDAO) {

  def saveMessage(message: JsObject): Unit = {
    println("saveMessage")
    println(message)
  }


}
