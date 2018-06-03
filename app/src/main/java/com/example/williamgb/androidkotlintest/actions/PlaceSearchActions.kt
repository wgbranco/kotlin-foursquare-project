package com.example.williamgb.androidkotlintest.actions

import com.example.williamgb.androidkotlintest.models.Model
import tw.geothings.rekotlin.Action

data class FoursquareSearchAction(val term: String): Action
data class FoursquareSearchStartedAction(val searchFor: String): Action
data class FoursquareSearchSuccessAction(val venues: Map<String, Model.Venue>): Action
data class FoursquareSearchFailuresAction(val error: Any): Action