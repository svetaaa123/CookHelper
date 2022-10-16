package ru.tanec.cookhelper.presentation.features.api.recipeApi.routing

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.tanec.cookhelper.core.constants.status.RecipeStatus
import ru.tanec.cookhelper.enterprise.model.entity.recipe.Recipe
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.CommentRepository
import ru.tanec.cookhelper.enterprise.repository.RecipeRepository
import ru.tanec.cookhelper.enterprise.repository.UserRepository
import ru.tanec.cookhelper.presentation.features.api.recipeApi.use_case.*


fun recipeApiRoutes(
    route: Routing,
    repository: RecipeRepository,
    userRepository: UserRepository,
    commentRepository: CommentRepository
) {

    route.post("/api/recipe/post/create") {
        call.respond(RecipeCreateUseCase(repository, userRepository, call.receiveMultipart().readAllParts()))

    }

    route.get("/api/recipe/get") {

        call.respond(GetRecipeUseCase(repository, call.request.queryParameters))

    }

    route.get("/api/recipe/get/image") {
        try {
            call.respondFile(GetRecipeImageUseCase(call.request.queryParameters))
        } catch (_:Exception) {
            call.respondFile(GetRecipeImageUseCase(Parameters.Empty))
        }
    }
    route.get("/api/recipe/post/like") {
        try {
            call.respond(LikeRecipeUseCase(repository, userRepository, call.receiveMultipart().readAllParts()))
        } catch (_:Exception) {
            call.respond(ApiResponse<Recipe>(RecipeStatus.EXCEPTION, "exception", null))
        }
    }

    route.get("/api/recipe/post/comment") {
        try {
            call.respond(CommentRecipeUseCase(repository, userRepository, commentRepository, call.receiveMultipart().readAllParts()))
        } catch (_:Exception) {
            call.respond(ApiResponse<Recipe>(RecipeStatus.EXCEPTION, "exception", null))
        }
    }

    route.get("/api/recipe/post/repost") {
        try {
            call.respond(RepostRecipeUseCase(repository, userRepository, call.receiveMultipart().readAllParts()))
        } catch (_:Exception) {
            call.respond(ApiResponse<Recipe>(RecipeStatus.EXCEPTION, "exception", null))
        }
    }

}
