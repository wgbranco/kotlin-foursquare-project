package com.example.williamgb.androidkotlintest.controllers

import android.app.Application
import com.chibatching.kotpref.Kotpref
import com.example.williamgb.androidkotlintest.middleware.foursquareMiddleware
import com.example.williamgb.androidkotlintest.middleware.venueMiddleware
import com.example.williamgb.androidkotlintest.reducers.appReducer
import com.example.williamgb.androidkotlintest.state.AppState
import com.example.williamgb.androidkotlintest.state.PlaceSearchState
import tw.geothings.rekotlin.Store

var mainStore = Store(
    state = null,
    reducer = ::appReducer,
    middleware = arrayListOf(foursquareMiddleware)
)

class AppController: Application() {

    override fun onCreate()
    {
        super.onCreate()

        Kotpref.init(this)

        val placeSearchState = PlaceSearchState()
        val state = AppState(placeSearchState = placeSearchState)
        mainStore = Store(
            state = state,
            reducer = ::appReducer,
            middleware = arrayListOf(
                foursquareMiddleware,
                venueMiddleware
            )
        )
    }
}