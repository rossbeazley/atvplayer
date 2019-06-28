package uk.co.rossbeazley.tviplayer

import java.io.Serializable

data class Episode (
    val title: String,
    val subtitle: String,
    val imageUrl: String,
    val backgroundImageUrl: String,
    val vpid: String
) : Serializable
{

}