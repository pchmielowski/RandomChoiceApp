package net.chmielowski.randomchoice.utils

internal sealed interface Loadable<T> {

    object Loading : Loadable<Nothing>

    data class Loaded<T>(val content: T) : Loadable<T>
}
