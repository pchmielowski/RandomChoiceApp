@file:Suppress("FunctionName")

package net.chmielowski.randomchoice.ui

import androidx.compose.animation.AnimatedContentScope.SlideDirection
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import net.chmielowski.randomchoice.core.Intent
import net.chmielowski.randomchoice.core.Label
import net.chmielowski.randomchoice.core.State
import net.chmielowski.randomchoice.persistence.ObserveSavedDilemmas
import net.chmielowski.randomchoice.ui.destinations.InputScreenDestination
import net.chmielowski.randomchoice.ui.destinations.SavedScreenDestination
import net.chmielowski.randomchoice.ui.screen.input.DropdownMenuStrategy
import net.chmielowski.randomchoice.ui.screen.input.InputScreen
import net.chmielowski.randomchoice.ui.screen.saved.SavedScreen
import net.chmielowski.randomchoice.ui.theme.AppTheme
import net.chmielowski.randomchoice.ui.theme.LocalTheme
import net.chmielowski.randomchoice.ui.theme.ThemePreference
import net.chmielowski.randomchoice.ui.theme.isDark

@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalMaterialNavigationApi::class, kotlinx.coroutines.ExperimentalCoroutinesApi::class,
)
@Composable
internal fun Content(
    preference: ThemePreference,
    observeSavedDilemmas: ObserveSavedDilemmas,
    store: Store<Intent, State, Label>,
    menuStrategy: DropdownMenuStrategy,
) {
    ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
        CompositionLocalProvider(LocalTheme provides preference.current) {
            AppTheme {
                Surface {
                    SetStatusBarContentColor()

                    DestinationsNavHost(
                        navGraph = NavGraphs.root,
                        engine = rememberAnimatedNavHostEngine(rootDefaultAnimations = animations()),
                    ) {
                        composable(InputScreenDestination) {
                            InputScreen(
                                navigator = destinationsNavigator,
                                store = store,
                                menuStrategy = menuStrategy,
                            )
                        }
                        composable(SavedScreenDestination) {
                            SavedScreen(
                                navigator = destinationsNavigator,
                                observeSavedDilemmas = observeSavedDilemmas,
                                labels = store.labels,
                                onIntent = store::accept,
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
private fun animations() = RootNavGraphDefaultAnimations(
    enterTransition = { slideIntoContainer(SlideDirection.Start) },
    exitTransition = { slideOutOfContainer(SlideDirection.Start) },
    popEnterTransition = { slideIntoContainer(SlideDirection.End) },
    popExitTransition = { slideOutOfContainer(SlideDirection.Start) },
)

@Composable
private fun SetStatusBarContentColor() {
    val controller = rememberSystemUiController()
    val isDarkTheme = isDark(LocalTheme.current)
    LaunchedEffect(controller, !isDarkTheme) {
        controller.statusBarDarkContentEnabled = !isDarkTheme
    }
}
