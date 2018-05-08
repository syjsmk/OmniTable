package domain.dao

import scala.concurrent.Future

trait DAO[T] {

  def getAll(): Future[Seq[T]]
  def insert(entity: T) : Future[Int]
  def get(id: Int): Future[Option[T]]
  def update(entity: T): Future[Option[T]]
  def update(id: Int, attrName: String, attrValue: Any): Future[Option[T]]
  def delete(id: Int): Future[Int]

}
