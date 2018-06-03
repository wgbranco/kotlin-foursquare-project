package com.example.williamgb.androidkotlintest.middleware

import android.util.Log
import com.example.williamgb.androidkotlintest.state.AppState
import com.example.williamgb.androidkotlintest.actions.FoursquareSearchAction
import com.example.williamgb.androidkotlintest.services.tasks.executeFoursquareSearchTask
import tw.geothings.rekotlin.Middleware

internal val foursquareMiddleware: Middleware<AppState> = { dispatch, getState ->
    { next ->
        { action ->
            when (action) {
                is FoursquareSearchAction -> {
                    executeFoursquareSearchTask(action, dispatch)
                }
            }
            next(action)
        }
    }
}