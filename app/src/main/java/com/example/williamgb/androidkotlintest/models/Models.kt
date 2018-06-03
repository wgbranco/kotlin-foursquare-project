    package com.example.williamgb.androidkotlintest.models

object Model {

    data class VenueLocation(
        val address: String,
        val lat: Double,
        val lng: Double,
        val distance: Int,
        val cc: String,
        val city: String,
        val state: String,
        val country: String,
        val formattedAddress: List<String>
    )

    data class VenueIcon(
        val prefix: String,
        val suffix: String
    )

    data class VenueCategories(
        val id: String,
        val name: String,
        val pluralName: String,
        val shortName: String,
        val icon: VenueIcon
    )

    data class Venue(
        val id: String,
        val name: String,
        val location: VenueLocation,
        val categories: List<VenueCategories>,
        val referralId: String,
        val hasPerk: String,
        val isFavourite: Boolean?
    )

    data class Meta (
        val code: Int,
        val requestId: String
    )

    data class Venues (
        val venues: List<Venue>
    )

    data class FoursquareResponse (
        val meta: Meta,
        val response: Venues
    )
}