package domain.dao

import com.google.inject.ImplementedBy
import domain.dao.impl.RoomDAOImpl
import domain.model.Room

@ImplementedBy(classOf[RoomDAOImpl])
trait RoomDAO extends DAO[Room] {
}
