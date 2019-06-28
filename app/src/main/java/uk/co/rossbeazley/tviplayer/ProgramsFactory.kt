package uk.co.rossbeazley.tviplayer

import org.json.JSONObject

class ProgramsFactory {

    fun parsePrograms(string: String): List<Program> {
        val listOf = mutableListOf<Program>()

        val topLevel = JSONObject(string)
        val category_programmes = topLevel.getJSONObject("category_programmes")
        val elements = category_programmes.getJSONArray("elements")
        for (i in 0 until elements.length()) {
            val obj = elements.get(i) as JSONObject
            val title = obj.getString("title")
            val id = obj.getString("id")
            val imageRecipe = obj.getJSONObject("images").getString("standard")
            val imageUrl = imageRecipe.replace("{recipe}", "314x176")
            val backgroundImageUrl = imageRecipe.replace("{recipe}", "1920x1080")

            val isSigned = obj.getJSONArray("initial_children").getJSONObject(0).getBoolean("signed")

            if (!isSigned) listOf.add(Program(title, imageUrl, backgroundImageUrl, id))
        }

        return listOf
    }
}