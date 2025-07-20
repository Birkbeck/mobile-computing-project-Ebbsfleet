package co.uk.bbk.culinarycompanion

import androidx.lifecycle.LiveData

class RecipeRepository(private val recipeDAO: RecipeDAO) {

    val allRecipes: LiveData<List<Recipe>> = recipeDAO.getAllRecipes()

    suspend fun insert(recipe: Recipe) = recipeDAO.insert(recipe)
    suspend fun update(recipe: Recipe) = recipeDAO.update(recipe)
    suspend fun delete(recipe: Recipe) = recipeDAO.delete(recipe)
}