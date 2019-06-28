package uk.co.rossbeazley.tviplayer

import org.json.JSONObject

class EpisodesFactory {
    fun parseEpisodes(json: String): List<Episode> {
        val listOf = mutableListOf<Episode>()

        val topLevel = JSONObject(json)
        val programme_episodes = topLevel.getJSONObject("programme_episodes")
        val elements = programme_episodes.getJSONArray("elements")

        for(i in 0 until elements.length())
        {
            val obj = elements.get(i) as JSONObject
            val title = obj.getString("title")
            val subtitle = obj.optString("subtitle")

            val imageRecipe = obj.getJSONObject("images").getString("standard")
            val imageUrl = imageRecipe.replace("{recipe}","314x176")
            val backgroundImageUrl = imageRecipe.replace("{recipe}","1920x1080")

            val vpid = obj.getJSONArray("versions").getJSONObject(0).getString("id")

            val isSigned = obj.getBoolean("signed")

            if( !isSigned ) listOf.add(Episode(title, subtitle, imageUrl, backgroundImageUrl, vpid))
        }

        return listOf
    }
}