package domain.dao

import scala.concurrent.Future

trait DAO[T] {

  def getAll(): Future[Seq[T]]
  def create(entity: T) : Future[Int]
  def get(id: Int): Future[Option[T]]
  def update(entity: T): Future[Int]
  def delete(id: Int): Future[Int]

}
