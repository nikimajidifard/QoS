package com.example.etta

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity


class ListAdapter(
    context: Context,
    dataArrayList: ArrayList<ListData?>?,
    private val onItemClick: (ListData) -> Unit
) : ArrayAdapter<ListData?>(context, R.layout.list_item, dataArrayList!!) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {

        var view = view
        val listData = getItem(position)

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        }

        val listImage = view!!.findViewById<ImageView>(R.id.listImage)
        val listName = view.findViewById<TextView>(R.id.listName)
        val btn_click_me = view.findViewById(R.id.btn_click_me) as Button

        listImage.setImageResource(listData!!.image)
        listName.text = listData.name



        btn_click_me.setOnClickListener {
            val pingData = pingg("web.eitaa.com")

            val intent = Intent(context, DetailedActivity::class.java)
            intent.putExtra("name", pingData.time)
            intent.putExtra("ingredients", pingData.description)
            intent.putExtra("desc", pingData.value)
            intent.putExtra("image", listData.image)
//
//            Log.d("ListAdapter", "pingData description: ${listData.image}")
//            Log.d("ListAdapter", "pingData description: ${pingData.description}")
//            Log.d("ListAdapter", "pingData description: ${pingData.time}")
//            Log.d("ListAdapter", "pingData description: ${pingData.value}")
//            Log.d("ListAdapter", "Starting DetailedActivity with Intent: $intent")

            context.startActivity(intent)


        }


        return view
    }

//    override fun onNewIntent(intent: Intent) {
//        super.onNewIntent(intent)
//        // Handle the new intent here
//        val pingData = pingg("web.eitaa.com")
//
//        val intent = Intent(context, DetailedActivity::class.java)
//        intent.putExtra("name", pingData.time)
//        intent.putExtra("ingredients", pingData.description)
//        intent.putExtra("desc", pingData.value)
//        intent.putExtra("image", listData.image)
//        context.startActivity(intent)
//
//    }
//


    fun pingg(domain: String): MainActivity.PingData {
        val runtime = Runtime.getRuntime()
        var timeofping: Long = 0
        try {
            val a = System.currentTimeMillis() % 100000
            val ipProcess = runtime.exec("/system/bin/ping -c 1 $domain")
            ipProcess.waitFor()
            Log.d("pingggggggggg", "Ping: $ipProcess ms")
            val b = System.currentTimeMillis() % 100000
            if (b <= a) {
                timeofping = (100000 - a) + b
            } else {
                timeofping = b - a
            }
        } catch (e: Exception) {
            // Handle the exception appropriately
        }

        // Determine the value and description based on the timeofping
        val value: String
        val description: String

        when {
            timeofping in 0..10 -> {
                value = "bad"
                description = "Very annoying"
            }
            timeofping in 10..20 -> {
                value = "poor"
                description = "Annoying"
            }
            timeofping in 20..30 -> {
                value = "fair"
                description = "Slightly annoying"
            }
            timeofping in 30..40 -> {
                value = "good"
                description = "Not annoying"
            }
            else -> {
                value = "excellent"
                description = "Imperceptible"
            }
        }

        return MainActivity.PingData(timeofping, value, description)
    }

}
