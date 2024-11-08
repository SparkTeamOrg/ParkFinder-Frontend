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

fun validatePhoneNumber(phoneNumber: String): Boolean {
    return android.util.Patterns.PHONE.matcher(phoneNumber).matches()
}

fun validateUserName(userName: String): Boolean {
    // Username validation logic:
    //      Must be between 4 and 50 characters long
    //      Must contain only letters (uppercase and lowercase) and exactly one space

    val userNameRegex = "^[A-Za-z]+\\s[A-Za-z]+$".toRegex()
    return userNameRegex.matches(userName)
}

fun validateLicencePlate(licencePlate: String): Boolean {
    // Licence plate validation logic:
    //      Must be in the format of two uppercase letters, three digits, and two uppercase letters
    val licencePlateRegex = "^[A-Z]{2}\\d{3}[A-Z]{2}\$".toRegex()
    return licencePlateRegex.matches(licencePlate)
}