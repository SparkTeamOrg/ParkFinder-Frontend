package com.app.parkfinder.ui.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import com.app.parkfinder.ui.screens.main.HelpCenterScreen
import com.app.parkfinder.ui.theme.ParkFinderTheme

class HelpCenterActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ParkFinderTheme {
                HelpCenterScreen (
                    onBackClick = { finish() }
                )
            }
        }
    }
}