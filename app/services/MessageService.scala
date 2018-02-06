package services

import javax.inject.Inject

import domain.dao.MessageDAO
import domain.dao.impl.MessageDAOImpl

class MessageService @Inject()(messageDAOImpl: MessageDAO) {

}
