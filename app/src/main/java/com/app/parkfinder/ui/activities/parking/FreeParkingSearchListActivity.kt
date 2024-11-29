package com.app.parkfinder.ui.activities.parking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.app.parkfinder.ui.screens.parking.ParkingScreenPreview
import com.app.parkfinder.ui.theme.ParkFinderTheme

class FreeParkingSearchListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ParkFinderTheme {
                ParkingScreenPreview()
            }
        }
    }

}
