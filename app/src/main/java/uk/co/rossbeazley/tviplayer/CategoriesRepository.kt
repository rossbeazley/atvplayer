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
            function( CategoriesFactory().parseCategories(it) )
        }, {

        })

        newRequestQueue.add(stringRequest)

    }

}