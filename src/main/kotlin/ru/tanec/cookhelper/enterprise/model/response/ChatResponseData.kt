package ru.tanec.cookhelper.enterprise.model.response

import ru.tanec.cookhelper.enterprise.model.entity.Message

data class ChatResponseData(
    val message: String,
    val status: Int,
    val data: Message
)