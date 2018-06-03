package com.example.williamgb.androidkotlintest.controllers

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.williamgb.androidkotlintest.R
import com.example.williamgb.androidkotlintest.models.Model
import com.squareup.picasso.Picasso

class VenueListAdapter(
    private val context: Context,
    private val dataSource: List<Model.Venue>
): BaseAdapter() {

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    //1
    override fun getCount(): Int {
        return dataSource.size
    }

    //2
    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    //3
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    //4
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View
    {
        // Get view for row item
        val rowView = inflater.inflate(R.layout.list_item_venue, parent, false)

        // Get title element
        val nameTextView = rowView.findViewById(R.id.nameTextView) as TextView

        // Get subtitle element
        val categoryTextView = rowView.findViewById(R.id.categoryTextView) as TextView

        // Get detail element
        val distanceTextView = rowView.findViewById(R.id.distanceTextView) as TextView

        // Get favorite/star element
        val starImageView = rowView.findViewById(R.id.starImageView) as ImageView

        // Get icon element
        val iconImageView = rowView.findViewById(R.id.iconImageView) as ImageView

        // Assign values to view elements
        val venue = getItem(position) as Model.Venue

        //Set name
        nameTextView.text = venue.name

        // Set distance text
        distanceTextView.text = venue.location?.distance?.toString() + "m"

        // Set favourite icon
        val starIcon = if (venue.isFavourite == true) R.drawable.ic_action_star else R.drawable.ic_action_star_border
        starImageView.setImageResource(starIcon)

        // Set venue icon/image
        var iconUrl = "-"
        var categoryName = "-"
        if (venue.categories.isNotEmpty()) {
            val category = venue.categories[0]
            categoryName = category.name
            iconUrl = category.icon.prefix + category.icon.suffix
        }
        categoryTextView.text = categoryName
        Picasso.get().load(iconUrl).placeholder(R.mipmap.ic_broken_image).into(iconImageView)

        return rowView
    }
}