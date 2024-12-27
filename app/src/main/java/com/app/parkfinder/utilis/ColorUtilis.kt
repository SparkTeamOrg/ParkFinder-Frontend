package com.app.parkfinder.utilis

class ColorUtilis {

    companion object {
        private val colorNames = mapOf(
            1 to "Red",
            2 to "Green",
            3 to "Blue",
            4 to "Yellow",
            5 to "Cyan",
            6 to "Magenta",
            7 to "Gray",
            8 to "Black"
        )

        fun getColorNames(): Map<Int, String> {
            return colorNames
        }

        fun getColorName(colorId: Int): String {
            return colorNames[colorId] ?: ""
        }

        fun getColorId(colorName: String): Int {
            return colorNames.filterValues { it == colorName }.keys.firstOrNull() ?: -1
        }
    }
}