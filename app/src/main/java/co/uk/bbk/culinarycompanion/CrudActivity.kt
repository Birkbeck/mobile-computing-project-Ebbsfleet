package co.uk.bbk.culinarycompanion

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.activity.viewModels

class CrudActivity : AppCompatActivity() {

    private val recipeViewModel: RecipeViewModel by viewModels()
    private lateinit var listView: ListView
    private lateinit var titleEdit: EditText
    private lateinit var ingredientsEdit: EditText
    private lateinit var instructionsEdit: EditText
    private lateinit var categoryEdit: EditText
    private lateinit var adapter: ArrayAdapter<String>
    private var recipeList = mutableListOf<Recipe>()
    var selectedRecipe: Recipe? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crud)

        ingredientsEdit = findViewById(R.id.editIngredients)
        instructionsEdit = findViewById(R.id.editInstructions)
        categoryEdit = findViewById(R.id.editCategory)
        titleEdit = findViewById(R.id.editTitle)

        listView = findViewById(R.id.listView)

        val addButton = findViewById<Button>(R.id.buttonAdd)
        val updateButton = findViewById<Button>(R.id.buttonUpdate)
        val deleteButton = findViewById<Button>(R.id.buttonDelete)

        var selectedRecipe: Recipe? = null

        recipeViewModel.allRecipes.observe(this, Observer { recipes ->
            recipeList = recipes.toMutableList()
            adapter.clear()
            adapter.addAll(recipes.map { "${it.title} (${it.ingredients}) (${it.instructions}) (${it.category})" })
            adapter.notifyDataSetChanged()
        })


        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf())
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            selectedRecipe = recipeList[position]
            titleEdit.setText(selectedRecipe?.title)
            ingredientsEdit.setText(selectedRecipe?.ingredients)
            instructionsEdit.setText(selectedRecipe?.instructions)
            categoryEdit.setText(selectedRecipe?.category)
        }

        addButton.setOnClickListener {
            val title = titleEdit.text.toString()
            val ingredients = ingredientsEdit.text.toString()
            val instructions = instructionsEdit.text.toString()
            val category = categoryEdit.text.toString()
            if (title.isNotEmpty() && ingredients.isNotEmpty() && instructions.isNotEmpty() && category.isNotEmpty()) {
                RecipeViewModel.insert(
                    Recipe(
                        title = title,
                        ingredients = ingredients,
                        instructions = instructions,
                        category = category
                    )
                )
                clearFields()
            }
        }

        updateButton.setOnClickListener {
            val title = titleEdit.text.toString()
            val ingredients = ingredientsEdit.text.toString()
            val instructions = instructionsEdit.text.toString()
            val category = categoryEdit.text.toString()
            selectedRecipe?.let {
                val updatedRecipe = it.copy(
                    title = title,
                    ingredients = ingredients,
                    instructions = instructions,
                    category = category
                )
                RecipeViewModel.update(updatedRecipe)
                clearFields()
            }
        }

        deleteButton.setOnClickListener {
            selectedRecipe?.let {
                RecipeViewModel.delete(it)
                clearFields()
            }
        }


        val goToMainButton: Button = findViewById(R.id.button2)
        goToMainButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }
    }
    private fun clearFields() {
        titleEdit.text.clear()
        ingredientsEdit.text.clear()
        instructionsEdit.text.clear()
        categoryEdit.text.clear()
        selectedRecipe = null
    }

}