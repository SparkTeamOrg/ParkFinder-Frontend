package com.app.parkfinder.utilis

fun validateEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun validatePassword(password: String): Boolean {
    // Password validation logic:
    //      Must be between 8 and 20 characters long
    //      Must contain at least one uppercase letter
    //      Must contain at least one lowercase letter
    //      Must contain at least one number
    //      Must contain at least one special character from @, $, !, %, *, ?, &, #, _
    val passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&#_])[A-Za-z\\d@\$!%*?&#_]{8,20}$".toRegex()
    return passwordRegex.matches(password)
}