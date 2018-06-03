package com.example.williamgb.androidkotlintest.reducers

import android.util.Log
import com.example.williamgb.androidkotlintest.actions.*
import com.example.williamgb.androidkotlintest.models.Model
import com.example.williamgb.androidkotlintest.state.AppState
import com.example.williamgb.androidkotlintest.state.PlaceSearchState
import tw.geothings.rekotlin.Action

fun appReducer(
    action: Action,
    oldState: AppState?
) : AppState {

    // if no state has been provided, create the default state
    val state = oldState ?: AppState(
            placeSearchState = PlaceSearchState()
    )

    return state.copy(
        placeSearchState = (::placeSearchReducer) (
            action,
            state.placeSearchState
        )
    )
}

fun placeSearchReducer(
    action: Action,
    state: PlaceSearchState?
): PlaceSearchState {

    val newState =  state ?: PlaceSearchState()

    when (action) {
        is FoursquareSearchStartedAction -> {
            return newState.copy(
                venues = emptyMap(),
                searchFor = action.searchFor,
                isFetching = true
            )
        }

        is FoursquareSearchSuccessAction -> {
            return newState.copy(
                venues = action.venues,
                isFetching = false,
                isComplete = true
            )
        }

        is FoursquareSearchFailuresAction -> {
            return newState.copy(
                errorMessage = action.error.toString(),
                isFetching = false,
                isComplete = true
            )
        }

        is VenueFavouriteStatusUpdate -> {
            val venue = newState.venues[action.venueId]

            if (venue?.id == null) return newState

            val newVenue = venue.copy(isFavourite = action.isFavourite)

            val newVenues = newState.venues.toMutableMap()
            newVenues[newVenue.id] = newVenue

            return newState.copy(
                venues = newVenues.toMap()
            )
        }
    }

    return newState
}