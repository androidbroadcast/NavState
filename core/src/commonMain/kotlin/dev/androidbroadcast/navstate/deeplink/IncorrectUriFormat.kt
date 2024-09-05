package dev.androidbroadcast.navstate.deeplink

public class IncorrectUriFormat : Exception {

    public constructor() : super()
    public constructor(message: String?) : super(message)
    public constructor(message: String?, cause: Throwable?) : super(message, cause)
    public constructor(cause: Throwable?) : super(cause)
}
