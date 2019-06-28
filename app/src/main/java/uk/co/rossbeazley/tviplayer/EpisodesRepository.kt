package uk.co.rossbeazley.tviplayer

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class EpisodesRepository(context: Context) {

    private val requestQueue = Volley.newRequestQueue(context)

    fun programEpisodes(id : String, function: (List<Episode>) -> Unit)
    {
        val ibl = "https://ibl.api.bbci.co.uk/ibl/v1/programmes/$id/episodes?rights=web&availability=available&per_page=200&sort=subtitle"
        val newRequestQueue = requestQueue

        val stringRequest = StringRequest(Request.Method.GET, ibl, {
                function( EpisodesFactory().parseEpisodes(it) )
        }, {

        })

        newRequestQueue.add(stringRequest)

    }


}