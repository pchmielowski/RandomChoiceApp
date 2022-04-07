package net.chmielowski.randomchoice.core

internal fun interface Choice {

    fun make(options: List<String>): Int
}

internal class RandomChoice : Choice {

    override fun make(options: List<String>) = options.indices.random()
}
