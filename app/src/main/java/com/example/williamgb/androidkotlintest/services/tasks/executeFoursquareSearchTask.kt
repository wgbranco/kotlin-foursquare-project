package com.example.williamgb.androidkotlintest.services.tasks

import com.example.williamgb.androidkotlintest.actions.*
import com.example.williamgb.androidkotlintest.services.network.FoursquareApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import tw.geothings.rekotlin.DispatchFunction

private var disposable: Disposable? = null

private val foursquareApiService by lazy {
    FoursquareApiService.create()
}

fun executeFoursquareSearchTask(
    action: FoursquareSearchAction,
    dispatch: DispatchFunction
) {
    dispatch(FoursquareSearchStartedAction(action.term))

    disposable = foursquareApiService
        .getSeattleVenues(action.term)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            {
                val venues = it.response.venues.map({
                    it.copy(isFavourite = venueIsFavourite(it.id))
                })

                val venuesMap = venues.map{ it.id to it}.toMap()

                dispatch(FoursquareSearchSuccessAction(venuesMap))
            },
            {
                error -> dispatch(FoursquareSearchFailuresAction(error))
            }
        )
}