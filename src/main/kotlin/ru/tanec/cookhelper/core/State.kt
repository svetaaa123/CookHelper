package ru.tanec.cookhelper.core


sealed class State<T>(
    val data: T? = null
) {

    class Success<T>(data: T? = null):  State<T>(data = data)

    class Error<T>(data: T? = null, message: String?): State<T>(data = data)

    class Interrupted<T>(data: T? = null): State<T>(data = data)

    class Processing<T>(data: T? = null): State<T>(data = data)


    class Expired<T>(data: T? = null, message: String?): State<T>(data = data)

}