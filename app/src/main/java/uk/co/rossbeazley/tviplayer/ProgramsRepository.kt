package uk.co.rossbeazley.tviplayer

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class ProgramsRepository(context: Context) {

    //coroutene it https://stackoverflow.com/questions/53486087/how-can-i-use-coroutines-with-volley-so-that-my-code-can-be-written-like-sychron

    private val requestQueue = Volley.newRequestQueue(context)

    fun programs(categoryId : String, function: (List<Program>) -> Unit)
    {
        val ibl = "https://ibl.api.bbci.co.uk/ibl/v1/categories/$categoryId/programmes?per_page=200&availability=available&sort=title"
        val newRequestQueue = requestQueue

        val stringRequest = StringRequest(Request.Method.GET, ibl, {
            function( ProgramsFactory().parsePrograms(it) )
        }, {

        })

        newRequestQueue.add(stringRequest)

    }

}