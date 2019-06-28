package uk.co.rossbeazley.tviplayer

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class CategoriesRepository(context: Context) {

    private val requestQueue = Volley.newRequestQueue(context)

    fun programs(function: (List<Category>) -> Unit)
    {
        val ibl = "https://ibl.api.bbci.co.uk/ibl/v1/categories/"
        val newRequestQueue = requestQueue

        val stringRequest = StringRequest(Request.Method.GET, ibl, {
            function( parseCategories(it) )
        }, {

        })

        newRequestQueue.add(stringRequest)

    }

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