package ru.tanec.cookhelper.enterprise.model.entity.chat

import kotlinx.serialization.Serializable

@Serializable
data class Chat(
    val id: Long,
    val members: List<Long>,
    val messages: List<Long>,
    val attachments: List<Long>,
    var avatar: List<String> = listOf()
)