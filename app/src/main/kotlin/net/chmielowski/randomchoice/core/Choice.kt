package net.chmielowski.randomchoice.core

internal fun interface Choice {

    fun make(options: List<Option>): Int
}

internal class RandomChoice : Choice {

    override fun make(options: List<Option>) = options.indices.random()
}
