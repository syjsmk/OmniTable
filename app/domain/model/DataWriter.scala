package domain.model

import play.api.libs.json.{Json, Writes}


// Model의 class들을 json으로 바꿀 writes는 여기에 일괄적으로 정의
class DataWriter {

  implicit val roomWrites = new Writes[Room] {
    override def writes(room: Room) = Json.obj(
      "id" -> room.id,
      "name" -> room.name,
      "userCount" -> room.userCount
    )
  }

}
