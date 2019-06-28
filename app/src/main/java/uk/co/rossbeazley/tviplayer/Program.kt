package uk.co.rossbeazley.tviplayer

import java.io.Serializable

data class Program(
    val title: String,
    val imageUrl: String,
    val backgroundImageUrl: String,
    val id: String
) : Serializable
{

}