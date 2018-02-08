package domain.dao

import com.google.inject.ImplementedBy
import domain.dao.impl.MessageDAOImpl
import domain.model.Message

@ImplementedBy(classOf[MessageDAOImpl])
trait MessageDAO extends DAO[Message] {

}
