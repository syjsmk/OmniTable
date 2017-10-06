package domain.dao

trait DAO[T] {

  def getAll(): Option[Seq[T]]
  def create(entity: T): Option[T]
  def findById(entity: T): Option[T]
  def get(id: Int): T

}
