package co.uk.bbk.culinarycompanion

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RecipeDAO {

    @Insert
    suspend fun insert(recipe: Recipe)

    @Update
    suspend fun update(recipe: Recipe)

    @Delete
    suspend fun delete(recipe: Recipe)

    @Query("SELECT * FROM Recipe ORDER BY id ASC")
    fun getAllRecipes(): LiveData<List<Recipe>>
}