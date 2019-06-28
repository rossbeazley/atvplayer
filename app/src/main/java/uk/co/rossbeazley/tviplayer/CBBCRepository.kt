package uk.co.rossbeazley.tviplayer

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class CBBCRepository(val context: Context) {

    //coroutene it https://stackoverflow.com/questions/53486087/how-can-i-use-coroutines-with-volley-so-that-my-code-can-be-written-like-sychron

    fun programs(function: (List<Program>) -> Unit)
    {
        ProgramsRepository(context).programs("cbbc",function)
    }

    fun programEpisodes(id : String, function: (List<Episode>) -> Unit)
    {
        EpisodesRepository(context).programEpisodes(id,function)
    }
}