package com.example.williamgb.androidkotlintest

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.williamgb.androidkotlintest.R.string.google_maps_key
import com.example.williamgb.androidkotlintest.models.Model
import com.example.williamgb.androidkotlintest.services.network.SEATTLE_CENTER_COORD
import com.example.williamgb.androidkotlintest.state.PlaceSearchState

import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_venue_details.*
import tw.geothings.rekotlin.StoreSubscriber
import android.util.DisplayMetrics
import com.example.williamgb.androidkotlintest.actions.ToggleVenueFavouriteStatus
import com.example.williamgb.androidkotlintest.controllers.mainStore


class VenueDetailsActivity : AppCompatActivity(), StoreSubscriber<PlaceSearchState> {

    companion object {
        const val EXTRA_ID = "id"

        fun newIntent(context: Context, venue: Model.Venue): Intent {
            val detailIntent = Intent(context, VenueDetailsActivity::class.java)

            detailIntent.putExtra(EXTRA_ID, venue.id)

            return detailIntent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_venue_details)

        mainStore.subscribe(this){
            it.select {
                it.placeSearchState
            }.skipRepeats { oldState, newState ->
                oldState == newState
            }
        }

        val venueId = intent.getStringExtra(EXTRA_ID)
        starImageView.setOnClickListener({
                mainStore.dispatch(ToggleVenueFavouriteStatus(venueId = venueId))
            }
        )
    }

    override fun newState(state: PlaceSearchState)
    {
        val venueId = intent.getStringExtra(EXTRA_ID)
        val venue = state.venues[venueId]

        if(venue?.id == null) return

        // Assign values to view elements

        nameTextView.text = venue.name

        // Set favourite icon

        val icon = if (venue.isFavourite == true) R.drawable.ic_action_star else R.drawable.ic_action_star_border
        starImageView.setImageResource(icon)

        // Full address

        addressTextView.text = venue?.location?.formattedAddress.joinToString(", ")

        // category

        categoryTextView.text = if (venue?.categories?.size > 0) venue.categories[0].name else "--"

        // Distance from center

        val distance = venue?.location?.distance
        distanceTextView.text = if (distance > 0) distance.toString()+"m" else "--"

        // Set static map

        val lat = venue?.location?.lat
        val lng = venue?.location?.lng

        val venueCoords = lat.toString()+","+lng.toString()

        // Get screen metrics
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)

        Log.e("DVA", metrics.toString())

        // Metrics in format: width x height
        val staticMapDimensions = (metrics.widthPixels).toString() + "x" + (metrics.heightPixels/2).toString()

        // Set static map zoom level based on distance
        val zoomLevel = if (distance > 10000) 10 else
                             if (distance > 2000) 12 else
                             if (distance > 500) 14 else 15

        // Build URL
        val staticMapUrl =
            "https://maps.googleapis.com/maps/api/staticmap?center=" + SEATTLE_CENTER_COORD +
            "&zoom=" + zoomLevel.toString() + "&size=" + staticMapDimensions + "&maptype=terrain" +
            "&markers=color:blue%7Clabel:S%7C" + SEATTLE_CENTER_COORD +
            "&markers=color:red%7Clabel:G%7C" + venueCoords +
            "&key=" + resources.getString(google_maps_key)

        Log.e("VDA", staticMapUrl)

        // Assign URL to ImageView
        Picasso.get().load(staticMapUrl).into(staticMapImageView)
    }

    override fun onDestroy()
    {
        mainStore.unsubscribe(this)
        super.onDestroy()
    }

}
