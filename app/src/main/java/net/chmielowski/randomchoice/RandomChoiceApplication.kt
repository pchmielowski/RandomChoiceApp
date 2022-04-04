package net.chmielowski.randomchoice

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@Suppress("unused")
class RandomChoiceApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setupLeakCanary()
        startKoin {
            androidContext(this@RandomChoiceApplication)
            modules(diModule)
        }
    }
}
