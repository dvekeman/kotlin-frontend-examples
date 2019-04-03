import kotlin.browser.window

fun main(args: Array<String>) {
    println("Hello JavaScript!")

    window.asDynamic().bar = barglobal
}

fun bar() : String {
    return "bar (from root package)"
}

val barglobal = {
    "bar (from global)"
}