package ru.tanec.cookhelper.presentation.features.websocket.topicsWebsocket.controller

import io.ktor.http.*
import io.ktor.server.websocket.*
import io.ktor.util.date.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.core.constants.status.*
import ru.tanec.cookhelper.database.dao.answerDao.ReplyDao
import ru.tanec.cookhelper.database.dao.answerDao.ReplyDaoImpl
import ru.tanec.cookhelper.database.dao.topicDao.TopicDao
import ru.tanec.cookhelper.database.dao.topicDao.TopicDaoImpl
import ru.tanec.cookhelper.database.dao.userDao.UserDao
import ru.tanec.cookhelper.database.dao.userDao.UserDaoImpl
import ru.tanec.cookhelper.enterprise.model.entity.forum.Reply
import ru.tanec.cookhelper.enterprise.model.entity.forum.Topic
import ru.tanec.cookhelper.enterprise.model.entity.user.User
import ru.tanec.cookhelper.enterprise.model.receive.topicWebsocket.ForumReceiveReplyData
import ru.tanec.cookhelper.enterprise.model.response.ReplyResponseData

class TopicConnectionController {
    val data: MutableMap<Long, MutableList<DefaultWebSocketServerSession>> = mutableMapOf()
    val topicDao: TopicDao = TopicDaoImpl()
    val replyDao: ReplyDao = ReplyDaoImpl()
    val userDao: UserDao = UserDaoImpl()

    suspend fun connect(
        session: DefaultWebSocketServerSession,
        parameters: Parameters
    ): Flow<State<Topic?>> = flow {
        emit(State.Processing())

        val id = parameters["id"]?.toLongOrNull()
        val token = parameters["token"]

        when (id == null) {
            true -> emit(State.Error(data=null, status=PARAMETER_MISSED))
            else -> {
                val topic = topicDao.getById(id)
                if (token == null) {
                    emit(State.Error(data=null, status=PARAMETER_MISSED))
                } else if (topic == null) {
                    emit(State.Error(data=null, status=TOPIC_NOT_FOUND))
                } else {
                    val sessionData = data[id]?: mutableListOf()
                    sessionData.add(session)
                    data[id] = sessionData
                    val user = userDao.getByToken(token)
                    emit(State.Success(data=topic, addition=user, status=SUCCESS))
                }
            }

        }


    }

    fun disconnect(session: DefaultWebSocketServerSession, id: Long): Boolean {
           return data[id]?.remove(session)?: return true
    }

    suspend fun sendMessage(
        reply: Reply,
        user: User,
        topicId: Long
    ) {

        val response = ReplyResponseData(
            id=reply.id,
            author=user.smallInfo(),
            text=reply.text,
            attachments=reply.attachments,
            replyToId=reply.replyToId,
            timestamp=reply.timestamp,
            ratingNegative = reply.ratingNegative,
            ratingPositive = reply.ratingPositive,
            replies = reply.replies.mapNotNull{replyDao.getById(it)}
        )

        for (receiver: DefaultWebSocketServerSession in data[topicId]?: listOf()) {
            receiver.sendSerialized(response)
        }


    }

    suspend fun receiveMessage(
        data: ForumReceiveReplyData,
        user: User
    ): State<Reply?> {
        val processedData = replyDao.insert(data.asDomain(user.id, listOf(), listOf(), getTimeMillis()))?: return State.Error(status=ANSWER_NOT_ADDED)
        return State.Success(data = processedData, status=SUCCESS)

    }
}