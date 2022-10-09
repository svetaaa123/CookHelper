package ru.tanec.cookhelper.enterprise.repository

import kotlinx.coroutines.flow.Flow
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.core.db.dao.recipeDao.RecipeDao
import ru.tanec.cookhelper.enterprise.model.entity.Recipe

interface RecipeRepository {
    val dao: RecipeDao

    fun insert(recipe: Recipe): Flow<State<Recipe?>>

    fun getById(id: Long): Flow<State<Recipe?>>

    fun getAll(id: Long, part: Int, div: Int): Flow<State<List<Recipe>>>
    fun getByUser(userId: Long, part: Int, div: Int): Flow<State<List<Recipe>>>

    fun getByTitle(title: String, part: Int, div: Int): Flow<State<List<Recipe>>>

    fun getRecipeByIngredient(ingredient: Long, part: Int, div: Int): Flow<State<List<Recipe>>>

    fun getRecipeByIngredients(ingredient: List<Long>, part: Int, div: Int): Flow<State<List<Recipe>>>

    fun editRecipe(recipe: Recipe): Flow<State<Recipe>>
}