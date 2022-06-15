package net.chmielowski.randomchoice

import net.chmielowski.randomchoice.core.Choice
import net.chmielowski.randomchoice.file.CreateFileImpl
import net.chmielowski.randomchoice.core.MainExecutor
import net.chmielowski.randomchoice.core.RandomChoice
import net.chmielowski.randomchoice.file.DeleteFile
import net.chmielowski.randomchoice.persistence.DeleteSavedDilemma
import net.chmielowski.randomchoice.persistence.DeleteSavedDilemmaImpl
import net.chmielowski.randomchoice.persistence.NonCancellableTask
import net.chmielowski.randomchoice.persistence.NonCancellableTaskImpl
import net.chmielowski.randomchoice.persistence.ObserveSavedDilemmas
import net.chmielowski.randomchoice.persistence.ObserveSavedDilemmasImpl
import net.chmielowski.randomchoice.persistence.SaveDilemma
import net.chmielowski.randomchoice.persistence.SaveDilemmaImpl
import net.chmielowski.randomchoice.persistence.UndeleteSavedDilemma
import net.chmielowski.randomchoice.persistence.UndeleteSavedDilemmaImpl
import net.chmielowski.randomchoice.persistence.createPersistentAndroidDatabase
import net.chmielowski.randomchoice.ui.theme.ThemePreference
import net.chmielowski.randomchoice.ui.theme.ThemePreferenceImpl
import org.koin.dsl.module

internal val diModule = module {
    single<ThemePreference> { ThemePreferenceImpl(get()) }
    factory<Choice> { RandomChoice() }

    // Database infrastructure
    single { createPersistentAndroidDatabase(get()) }
    factory<NonCancellableTask> { NonCancellableTaskImpl() }

    // Database actions
    factory<SaveDilemma> { SaveDilemmaImpl(get(), get()) }
    factory<ObserveSavedDilemmas> { ObserveSavedDilemmasImpl(get()) }
    factory<DeleteSavedDilemma> { DeleteSavedDilemmaImpl(get(), get()) }
    factory<UndeleteSavedDilemma> { UndeleteSavedDilemmaImpl(get(), get()) }

    // File system
    factory { CreateFileImpl(get()) }
    factory { DeleteFile() }

    factory { MainExecutor.Factory { MainExecutor(get(), get(), get(), get(), get(), get(), get()) } }
}
