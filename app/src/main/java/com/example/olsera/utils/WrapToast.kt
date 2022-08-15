package com.example.olsera.utils

import android.app.Activity
import android.widget.TextView
import android.widget.Toast
import com.example.olsera.R

fun Toast.showCustomToast(message: String, activity: Activity)
{
    val layout = activity.layoutInflater.inflate (
        R.layout.custom_toast_layout,
        activity.findViewById(R.id.toast_container)
    )

    // set the text of the TextView of the message
    val textView = layout.findViewById<TextView>(R.id.toast_text)
    textView.text = message

    // use the application extension function
    this.apply {
//        setGravity(Gravity.BOTTOM, 0, 40)
        duration = Toast.LENGTH_LONG
        view = layout
        show()
    }
}