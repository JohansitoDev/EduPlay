package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.AppDestination
import com.example.model.AppTab
import com.example.ui.components.CelebrationDialog
import com.example.ui.components.EduPlayBottomBar
import com.example.ui.screens.AlphabetCourseScreen
import com.example.ui.screens.AnimalQuizScreen
import com.example.ui.screens.GamesTabScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.MatchItScreen
import com.example.ui.screens.NumberQuizScreen
import com.example.ui.screens.ProfileTabScreen
import com.example.ui.screens.RankingTabScreen
import com.example.ui.screens.StreakDetailScreen
import com.example.ui.screens.TraceLetterScreen
import com.example.ui.screens.WordBuilderScreen
import com.example.ui.theme.EduPlayTheme
import com.example.util.SoundManager
import com.example.viewmodel.EduPlayViewModel

class MainActivity : ComponentActivity() {
    private var soundManager: SoundManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        soundManager = SoundManager(this)

        setContent {
            EduPlayTheme {
                EduPlayRoot(soundManager = soundManager)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        soundManager?.shutdown()
        soundManager = null
    }
}

@Composable
fun EduPlayRoot(
    soundManager: SoundManager?,
    viewModel: EduPlayViewModel = viewModel()
) {
    val userEntity by viewModel.userEntity.collectAsStateWithLifecycle()

    if (userEntity == null) {
        LoginScreen(
            onLogin = { username, password ->
                viewModel.login(username, password)
            }
        )
    } else {
        EduPlayApp(soundManager = soundManager, viewModel = viewModel)
    }
}

@Composable
fun EduPlayApp(
    soundManager: SoundManager?,
    viewModel: EduPlayViewModel
) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val destination by viewModel.destination.collectAsStateWithLifecycle()
    val stats by viewModel.stats.collectAsStateWithLifecycle()
    val profile by viewModel.userProfile.collectAsStateWithLifecycle()
    val celebrationText by viewModel.celebrationText.collectAsStateWithLifecycle()
    val soundEnabled by viewModel.soundEnabled.collectAsStateWithLifecycle()
    val notificationsEnabled by viewModel.notificationsEnabled.collectAsStateWithLifecycle()

    // Back handling: pop sub-screens back to main tabs
    BackHandler(enabled = destination != AppDestination.MainTabs) {
        viewModel.navigateBack()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (destination == AppDestination.MainTabs) {
                EduPlayBottomBar(
                    currentTab = currentTab,
                    onTabSelected = { tab ->
                        viewModel.selectTab(tab)
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = destination,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "screen_transition"
            ) { targetDest ->
                when (targetDest) {
                    is AppDestination.MainTabs -> {
                        when (currentTab) {
                            AppTab.HOME -> HomeScreen(
                                stats = stats,
                                profile = profile,
                                onNavigate = { viewModel.navigateTo(it) },
                                onSelectTab = { viewModel.selectTab(it) },
                                onToggleProfile = { viewModel.logout() }
                            )
                            AppTab.GAMES -> GamesTabScreen(
                                stats = stats,
                                onNavigate = { viewModel.navigateTo(it) }
                            )
                            AppTab.REWARD -> RankingTabScreen(
                                leaderboard = viewModel.leaderboardEntries,
                                badges = viewModel.badges
                            )
                            AppTab.PROFILE -> ProfileTabScreen(
                                profile = profile,
                                stats = stats,
                                soundEnabled = soundEnabled,
                                notificationsEnabled = notificationsEnabled,
                                soundManager = soundManager,
                                onToggleSound = { viewModel.toggleSound(it) },
                                onToggleNotifications = { viewModel.toggleNotifications(it) },
                                onToggleProfile = { viewModel.logout() }
                            )
                        }
                    }
                    is AppDestination.AlphabetCourse -> AlphabetCourseScreen(
                        soundManager = soundManager,
                        onNavigate = { viewModel.navigateTo(it) },
                        onBack = { viewModel.navigateBack() }
                    )
                    is AppDestination.TraceLetter -> TraceLetterScreen(
                        letter = targetDest.letter,
                        viewModel = viewModel,
                        soundManager = soundManager,
                        onClose = { viewModel.navigateBack() }
                    )
                    is AppDestination.WordBuilder -> WordBuilderScreen(
                        viewModel = viewModel,
                        soundManager = soundManager,
                        onClose = { viewModel.navigateBack() }
                    )
                    is AppDestination.AnimalQuiz -> AnimalQuizScreen(
                        viewModel = viewModel,
                        soundManager = soundManager,
                        onClose = { viewModel.navigateBack() }
                    )
                    is AppDestination.NumberQuiz -> NumberQuizScreen(
                        viewModel = viewModel,
                        soundManager = soundManager,
                        onClose = { viewModel.navigateBack() }
                    )
                    is AppDestination.MatchIt -> MatchItScreen(
                        viewModel = viewModel,
                        soundManager = soundManager,
                        onClose = { viewModel.navigateBack() }
                    )
                    is AppDestination.StreakDetail -> StreakDetailScreen(
                        stats = stats,
                        onBack = { viewModel.navigateBack() }
                    )
                    is AppDestination.BadgesList -> RankingTabScreen(
                        leaderboard = viewModel.leaderboardEntries,
                        badges = viewModel.badges
                    )
                }
            }

            // Reward / Achievement Popup Dialog
            CelebrationDialog(
                message = celebrationText,
                onDismiss = { viewModel.dismissCelebration() }
            )
        }
    }
}
