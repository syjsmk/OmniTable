package domain.dao.impl

import javax.inject.Inject

import domain.dao.MessageDAO
import domain.model.{Message, Messages}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class MessageDAOImpl @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends MessageDAO {


  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  val messages = TableQuery[Messages]

  override def getAll(): Future[Seq[Message]] = {

    db.run(messages.result)

  }

  override def create(entity: Message) = ???

  override def get(id: Int) = ???

  override def update(entity: Message) = ???

  override def update(id: Int, attrName: String, attrValue: Any) = ???

  override def delete(id: Int) = ???

}
