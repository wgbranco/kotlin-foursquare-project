package com.example.williamgb.androidkotlintest.state

import tw.geothings.rekotlin.StateType

data class AppState(
   var placeSearchState: PlaceSearchState
): StateType
