package com.app.parkfinder.ui.activities.parking

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import com.app.parkfinder.logic.models.dtos.ParkingLotDto
import com.app.parkfinder.logic.models.dtos.ParkingSpotDto
import com.app.parkfinder.ui.activities.BaseActivity
import com.app.parkfinder.ui.screens.main.ReservationScreen
import com.app.parkfinder.ui.theme.ParkFinderTheme

class ReservationActivity : BaseActivity() {

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val spot = intent.getParcelableExtra("parking_spot", ParkingSpotDto::class.java)
            val lot = intent.getParcelableExtra("parking_lot", ParkingLotDto::class.java)
            val spotNumber = intent.getStringExtra("spot_number")

            Log.d("Debug", lot.toString())

            ParkFinderTheme {
                if (lot != null && spotNumber != null) {
                    ReservationScreen(
                        lot = lot,
                        spotNumber = spotNumber
                    )
                }
            }
        }
    }

}