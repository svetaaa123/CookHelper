package ru.tanec.cookhelper.enterprise.model.receive.postApi

import io.ktor.http.content.*

data class PostData(
    val owner: Long?,
    val text: String? = "",
    val attachment: List<PartData.FileItem>,
    val images: List<PartData.FileItem>,
)