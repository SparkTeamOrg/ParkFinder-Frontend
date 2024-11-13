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
    val passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&#_])[\\p{L}\\p{N}@\$!%*?&#_]{8,20}\$".toRegex()
    return passwordRegex.matches(password)
}

fun validatePhoneNumber(phoneNumber: String): Boolean {
    return android.util.Patterns.PHONE.matcher(phoneNumber).matches()
}

fun validateUserName(userName: String): Boolean {
    // Username validation logic:
    //      Must be between 4 and 50 characters long
    //      Must contain only letters (uppercase and lowercase) and exactly one space

    val userNameRegex = "^\\p{Lu}\\p{L}+\\s\\p{Lu}\\p{L}+$".toRegex()
    return userNameRegex.matches(userName)
}

fun validateLicencePlate(licencePlate: String): Boolean {
    // Licence plate validation logic:
    //      Must be in the format of two uppercase letters, three digits, and two uppercase letters
    val licencePlateRegex = "^\\p{Lu}{2}\\d{3}\\p{Lu}{2}$".toRegex()
    return licencePlateRegex.matches(licencePlate)
}


public fun uppercaseCheck(pass: String): Boolean
{
    val passRegex = ".*[A-Z].*".toRegex()
    return passRegex.matches(pass)
}

public fun lengthCheck(pass: String): Boolean
{
    return pass.length in 8..20
}

public fun lowercaseCheck(pass: String): Boolean
{
    val passRegex = ".*[a-z].*".toRegex()
    return passRegex.matches(pass)
}
public fun digitCheck(pass: String): Boolean
{
    val passRegex = ".*[0-9].*".toRegex()
    return passRegex.matches(pass)
}
public fun specialCharCheck(pass: String): Boolean
{
    val passRegex = ".*[@_!#\$%&*?].*".toRegex()
    return passRegex.matches(pass)
}