package com.example.williamgb.androidkotlintest.actions

import tw.geothings.rekotlin.Action

data class ToggleVenueFavouriteStatus(val venueId: String): Action
data class VenueFavouriteStatusUpdate(val venueId: String, val isFavourite: Boolean): Action