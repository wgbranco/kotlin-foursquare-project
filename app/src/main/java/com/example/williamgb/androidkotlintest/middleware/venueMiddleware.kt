package com.example.williamgb.androidkotlintest.middleware

import com.example.williamgb.androidkotlintest.actions.ToggleVenueFavouriteStatus
import com.example.williamgb.androidkotlintest.services.tasks.updateFavouriteVanues
import com.example.williamgb.androidkotlintest.state.AppState
import tw.geothings.rekotlin.Middleware

internal val venueMiddleware: Middleware<AppState> = { dispatch, getState ->
    { next ->
        { action ->
            when (action) {
                is ToggleVenueFavouriteStatus -> {
                    updateFavouriteVanues(action, dispatch)
                }
            }
            next(action)
        }
    }
}