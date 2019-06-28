package uk.co.rossbeazley.tviplayer

import org.json.JSONObject

class CategoriesFactory
{

    fun parseCategories(string : String) : List<Category>
    {
        val listOf = mutableListOf<Category>()

        val topLevel = JSONObject(string)
        val elements = topLevel.getJSONArray("categories")
        for(i in 0 until elements.length())
        {
            val obj = elements.get(i) as JSONObject
            val title = obj.getString("title")
            val id = obj.getString("id")

            val notGenre = "genre" == obj.getString("kind")

            if( notGenre ) listOf.add(Category(title, id))
        }

        return listOf
    }
}