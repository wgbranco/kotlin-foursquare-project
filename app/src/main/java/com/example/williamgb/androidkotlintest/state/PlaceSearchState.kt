package com.example.williamgb.androidkotlintest.state

import com.example.williamgb.androidkotlintest.models.Model
import tw.geothings.rekotlin.StateType

data class PlaceSearchState(
    var searchFor: String? = null,
    var isFetching: Boolean = false,
    var isComplete: Boolean = false,
    var errorMessage: String? = null,
    var venues: Map<String, Model.Venue> = emptyMap()
): StateType