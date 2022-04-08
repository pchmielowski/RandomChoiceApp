package net.chmielowski.randomchoice.core

internal fun interface Choice {

    // TODO@ String -> Option
    fun make(options: List<String>): Int
}

internal class RandomChoice : Choice {

    override fun make(options: List<String>) = options.indices.random()
}
