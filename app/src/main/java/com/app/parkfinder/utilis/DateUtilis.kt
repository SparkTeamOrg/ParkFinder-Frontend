package com.app.parkfinder.utilis

import java.text.SimpleDateFormat
import java.util.Locale

fun formatDate(dateString: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSS", Locale.getDefault())
    val outputFormat = SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault())

    val date = inputFormat.parse(dateString)
    return if(date!=null){
        outputFormat.format(date).toString()
    }
    else{ "Error" }
}

fun formatDateFirstDate(dateString: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSS", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    val date = inputFormat.parse(dateString)
    return if(date!=null){
        outputFormat.format(date).toString()
    }
    else{ "Error" }
}