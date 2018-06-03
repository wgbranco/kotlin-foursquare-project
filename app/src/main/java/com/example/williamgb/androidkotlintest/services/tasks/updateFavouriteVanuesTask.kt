package com.example.williamgb.androidkotlintest.services.tasks

import com.chibatching.kotpref.KotprefModel
import com.example.williamgb.androidkotlintest.actions.ToggleVenueFavouriteStatus
import com.example.williamgb.androidkotlintest.actions.VenueFavouriteStatusUpdate
import tw.geothings.rekotlin.DispatchFunction

import java.util.*

object FavouriteVenues: KotprefModel()
{
    val ids by stringSetPref {
        val set = TreeSet<String>()
        return@stringSetPref set
    }
}

fun venueIsFavourite(venueId: String): Boolean {
    return FavouriteVenues.ids.contains(venueId)
}

fun removeFromFavourites(venueId: String): Boolean {
    return FavouriteVenues.ids.remove(venueId)
}

fun addToFavourites(venueId: String): Boolean {
    return FavouriteVenues.ids.add(venueId)
}

fun updateFavouriteVanues(
    action: ToggleVenueFavouriteStatus,
    dispatch: DispatchFunction
) {
    val venueId = action.venueId

    val wasInSet = removeFromFavourites(venueId)
    if (!wasInSet) addToFavourites(venueId)

    dispatch(VenueFavouriteStatusUpdate(venueId = venueId, isFavourite = venueIsFavourite(venueId)))
}