package net.chmielowski.randomchoice.persistence

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

interface NonCancellableTask {

    suspend fun <T> run(action: suspend () -> T): T

    fun <T> runNotObserving(action: suspend () -> T)

    companion object {

        val fake = object : NonCancellableTask {
            override suspend fun <T> run(action: suspend () -> T) = action()

            override fun <T> runNotObserving(action: suspend () -> T) {
                runBlocking { action() }
            }
        }
    }
}

class NonCancellableTaskImpl(private val nonCancellableScope: CoroutineScope = nonCancellableScope()) :
    NonCancellableTask {

    override suspend fun <T> run(action: suspend () -> T) =
        suspendCancellableCoroutine<T> { observedContinuation ->
            nonCancellableScope.launch {
                val result = action()
                observedContinuation.resume(result)
            }
        }

    override fun <T> runNotObserving(action: suspend () -> T) {
        nonCancellableScope.launch {
            action()
        }
    }
}

private fun nonCancellableScope() = CoroutineScope(Job() + Dispatchers.Main)
