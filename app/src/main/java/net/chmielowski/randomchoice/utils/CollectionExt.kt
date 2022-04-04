package net.chmielowski.randomchoice.utils

internal fun <E> Collection<E>.replace(index: Int, value: E): List<E> {
    val mutable = toMutableList()
    mutable[index] = value
    return mutable.toList()
}

internal fun <E> Collection<E>.removeIndex(indexToRemove: Int) =
    filterIndexed { index, _ -> index != indexToRemove }
