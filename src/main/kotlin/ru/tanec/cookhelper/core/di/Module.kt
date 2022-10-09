package ru.tanec.cookhelper.core.di

import io.ktor.server.websocket.*
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.tanec.cookhelper.core.db.repository.RecipeRepositoryImpl
import ru.tanec.cookhelper.core.db.repository.UserRepositoryImpl
import ru.tanec.cookhelper.enterprise.repository.ConnectionRepository
import ru.tanec.cookhelper.enterprise.repository.RecipeRepository
import ru.tanec.cookhelper.enterprise.repository.UserRepository
import ru.tanec.cookhelper.presentation.features.websocket.chatWebsocket.controller.ChatConnectionController
import ru.tanec.cookhelper.presentation.features.websocket.chatWebsocket.repository.ChatConnectionRepository

val managerModule = module {
    singleOf(::ChatConnectionController)
    singleOf(::ChatConnectionRepository) { bind<ConnectionRepository<Long>>() }
    single { mutableMapOf<Long, MutableList<DefaultWebSocketServerSession>>() }
}

val apiRepositoryModule = module {
    single<UserRepository> {UserRepositoryImpl()}
    single<RecipeRepository> {RecipeRepositoryImpl()}
}