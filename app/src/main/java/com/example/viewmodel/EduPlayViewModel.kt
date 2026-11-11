package com.example.viewmodel

import android.app.Application
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.R
import com.example.data.local.EduPlayDatabase
import com.example.data.local.UserEntity
import com.example.data.local.UserRepository
import com.example.model.AppDestination
import com.example.model.AppTab
import com.example.model.BadgeData
import com.example.model.LeaderboardEntry
import com.example.model.UserProfile
import com.example.model.UserStats
import com.example.util.NotificationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class WordBuilderState(
    val wordIndex: Int = 0,
    val targetWord: String = "VACA",
    val hintText: String = "¡Un animal que nos da leche! 🥛",
    val letterSlots: List<Char?> = listOf('V', 'A', 'C', 'A', null),
    val letterBank: List<Char> = listOf('A', 'H', 'W', 'D', 'E', 'B', 'M', 'S', 'V', 'C'),
    val isCorrect: Boolean? = null,
    val remainingHints: Int = 3
)

data class AnimalQuizState(
    val currentQuestionIndex: Int = 0,
    val animalName: String = "Jirafa",
    val choices: List<String> = listOf("Elefante", "Jirafa", "Cebra", "León"),
    val selectedChoice: String? = "Jirafa",
    val isAnswerChecked: Boolean = false,
    val isCorrect: Boolean = true
)

data class NumberQuizState(
    val count: Int = 7,
    val choices: List<Int> = listOf(5, 6, 7, 8),
    val selectedChoice: Int? = 7,
    val isAnswerChecked: Boolean = false,
    val isCorrect: Boolean = true
)

data class MatchItState(
    val selectedLeft: String? = "León",
    val matches: Map<String, String> = mapOf("León" to "León"),
    val isSubmitted: Boolean = false
)

class EduPlayViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository: UserRepository

    init {
        val userDao = EduPlayDatabase.getDatabase(application).userDao()
        userRepository = UserRepository(userDao)
        viewModelScope.launch {
            userRepository.currentUser.collect { entity ->
                if (entity != null) {
                    _userEntity.value = entity
                    _stats.value = UserStats(
                        dayStreak = entity.dayStreak,
                        totalXp = entity.totalXp,
                        badgesCount = entity.badgesCount,
                        gemsCount = entity.gemsCount,
                        lessonsCompleted = entity.lessonsCompleted,
                        gamesPlayed = entity.gamesPlayed,
                        longestStreak = entity.dayStreak + 5,
                        dailyGoalCompleted = 2,
                        dailyGoalTotal = 3,
                        livesCount = entity.livesCount
                    )
                    _userProfile.value = UserProfile(
                        name = entity.name,
                        fullName = entity.fullName,
                        handle = entity.handle,
                        joinedYear = "2024",
                        avatarRes = R.drawable.img_avatar_emma
                    )
                    // Mostrar notificación persistente de vidas restantes
                    NotificationHelper.showPersistentLivesNotification(application, entity.livesCount)
                } else {
                    _userEntity.value = null
                }
            }
        }
    }

    private val _userEntity = MutableStateFlow<UserEntity?>(null)
    val userEntity: StateFlow<UserEntity?> = _userEntity.asStateFlow()

    private val _currentTab = MutableStateFlow(AppTab.HOME)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    private val _destination = MutableStateFlow<AppDestination>(AppDestination.MainTabs)
    val destination: StateFlow<AppDestination> = _destination.asStateFlow()

    private val _stats = MutableStateFlow(UserStats())
    val stats: StateFlow<UserStats> = _stats.asStateFlow()

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _soundEnabled = MutableStateFlow(true)
    val soundEnabled: StateFlow<Boolean> = _soundEnabled.asStateFlow()

    private val _notificationsEnabled = MutableStateFlow(true)
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled.asStateFlow()

    // Tracing paths
    private val _tracedPoints = MutableStateFlow<List<Offset>>(emptyList())
    val tracedPoints: StateFlow<List<Offset>> = _tracedPoints.asStateFlow()

    // Word Builder
    private val _wordBuilderState = MutableStateFlow(WordBuilderState())
    val wordBuilderState: StateFlow<WordBuilderState> = _wordBuilderState.asStateFlow()

    // Animal Quiz
    private val _animalQuizState = MutableStateFlow(AnimalQuizState())
    val animalQuizState: StateFlow<AnimalQuizState> = _animalQuizState.asStateFlow()

    // Number Quiz
    private val _numberQuizState = MutableStateFlow(NumberQuizState())
    val numberQuizState: StateFlow<NumberQuizState> = _numberQuizState.asStateFlow()

    // Match It
    private val _matchItState = MutableStateFlow(MatchItState())
    val matchItState: StateFlow<MatchItState> = _matchItState.asStateFlow()

    // Celebration modal
    private val _celebrationText = MutableStateFlow<String?>(null)
    val celebrationText: StateFlow<String?> = _celebrationText.asStateFlow()

    // Leaderboard list
    val leaderboardEntries = listOf(
        LeaderboardEntry(1, "Emma", "Super Aprendiz", 2450, false, "👧"),
        LeaderboardEntry(2, "Alex (Tú)", "Estrella Matemática", 1980, true, "👦"),
        LeaderboardEntry(3, "Liam", "Mago de los Números", 1760, false, "🧒"),
        LeaderboardEntry(4, "Sofía", "Exploradora de Palabras", 1520, false, "👧"),
        LeaderboardEntry(5, "Noah", "Experto en Animales", 1380, false, "👦")
    )

    // Badges list
    val badges = listOf(
        BadgeData("starter", "Principiante", "⭐", 0xFFFFB703, "¡Completaste tu primera lección!"),
        BadgeData("quick", "Aprendiz Rápido", "⚡", 0xFFFB8500, "¡Respondiste 5 preguntas seguidas!"),
        BadgeData("top_scorer", "Mejor Puntuación", "👑", 0xFF8338EC, "¡100% de aciertos en un cuestionario!", "hace 2h"),
        BadgeData("helper", "Ayudante", "💚", 0xFF06D6A0, "¡Practicaste 7 días seguidos!"),
        BadgeData("champion", "Campeón", "🏆", 0xFFEF476F, "¡Completaste todas las lecciones del alfabeto!")
    )

    fun login(username: String, password: String) {
        viewModelScope.launch {
            userRepository.loginOrRegister(username, password)
        }
    }

    fun logout() {
        viewModelScope.launch {
            val current = _userEntity.value
            if (current != null) {
                userRepository.logout(current.username)
            }
            _userEntity.value = null
        }
    }

    fun selectTab(tab: AppTab) {
        _currentTab.value = tab
        _destination.value = AppDestination.MainTabs
    }

    fun navigateTo(dest: AppDestination) {
        _destination.value = dest
    }

    fun navigateBack(): Boolean {
        if (_destination.value != AppDestination.MainTabs) {
            _destination.value = AppDestination.MainTabs
            return true
        }
        return false
    }

    fun toggleSound(enabled: Boolean) {
        _soundEnabled.value = enabled
    }

    fun toggleNotifications(enabled: Boolean) {
        _notificationsEnabled.value = enabled
    }

    fun addTracedPoint(point: Offset) {
        _tracedPoints.update { it + point }
    }

    fun clearTracing() {
        _tracedPoints.value = emptyList()
    }

    fun completeTracing() {
        if (_stats.value.livesCount <= 0) {
            showCelebration("¡Te has quedado sin vidas por hoy! Vuelve mañana para más vidas. ⏳")
            return
        }
        addXpAndPersist(50, 5, 0, 1, 0)
        showCelebration("¡Excelente trabajo trazando! ¡Letra dominada! ⭐")
    }

    // Word builder actions
    fun tapBankLetter(letter: Char) {
        val current = _wordBuilderState.value
        val slots = current.letterSlots.toMutableList()
        val emptyIndex = slots.indexOfFirst { it == null }
        if (emptyIndex != -1) {
            slots[emptyIndex] = letter
            val bank = current.letterBank.toMutableList()
            bank.remove(letter)
            _wordBuilderState.value = current.copy(letterSlots = slots, letterBank = bank)
        }
    }

    fun tapSlotLetter(index: Int) {
        val current = _wordBuilderState.value
        val char = current.letterSlots.getOrNull(index) ?: return
        val slots = current.letterSlots.toMutableList()
        slots[index] = null
        val bank = current.letterBank.toMutableList()
        bank.add(char)
        _wordBuilderState.value = current.copy(letterSlots = slots, letterBank = bank)
    }

    fun useHint() {
        val current = _wordBuilderState.value
        if (current.remainingHints > 0) {
            _wordBuilderState.value = current.copy(
                remainingHints = current.remainingHints - 1
            )
        }
    }

    fun checkWordBuilder(): Boolean {
        if (_stats.value.livesCount <= 0) {
            showCelebration("¡Te has quedado sin vidas por hoy! Vuelve mañana para más vidas. ⏳")
            return false
        }
        val current = _wordBuilderState.value
        val assembled = current.letterSlots.filterNotNull().joinToString("")
        val isMatch = assembled.equals(current.targetWord, ignoreCase = true)
        _wordBuilderState.value = current.copy(isCorrect = isMatch)
        if (isMatch) {
            addXpAndPersist(100, 10, 0, 1, 1)
            showCelebration("¡Asombroso! ¡Formaste '${current.targetWord}'! 🎉")
        }
        return isMatch
    }

    fun switchWord(index: Int) {
        if (index == 1) {
            _wordBuilderState.value = WordBuilderState(
                wordIndex = 1,
                targetWord = "ZEBRA",
                hintText = "¡Un hermoso animal con rayas blancas y negras! 🦓",
                letterSlots = listOf('Z', 'E', 'B', 'R', null),
                letterBank = listOf('A', 'O', 'Z', 'I', 'E', 'R', 'B', 'U', 'F', 'T')
            )
        } else {
            _wordBuilderState.value = WordBuilderState()
        }
    }

    // Animal Quiz actions
    fun selectAnimalChoice(choice: String) {
        _animalQuizState.update { it.copy(selectedChoice = choice) }
    }

    fun continueAnimalQuiz() {
        if (_stats.value.livesCount <= 0) {
            showCelebration("¡Te has quedado sin vidas por hoy! Vuelve mañana para más vidas. ⏳")
            return
        }
        val current = _animalQuizState.value
        val isCorrect = current.selectedChoice == current.animalName
        if (isCorrect) {
            addXpAndPersist(50, 5, 0, 1, 1)
            showCelebration("¡Correcto! ¡Es una amigable ${current.animalName}! 🌟")
        }
    }

    // Number Quiz actions
    fun selectNumberChoice(number: Int) {
        _numberQuizState.update { it.copy(selectedChoice = number) }
    }

    fun continueNumberQuiz() {
        if (_stats.value.livesCount <= 0) {
            showCelebration("¡Te has quedado sin vidas por hoy! Vuelve mañana para más vidas. ⏳")
            return
        }
        val current = _numberQuizState.value
        if (current.selectedChoice == current.count) {
            addXpAndPersist(50, 5, 0, 1, 1)
            showCelebration("¡Exacto! ¡Hay 7 jugosas manzanas! 🍎")
        }
    }

    // Match It actions
    fun connectMatch(animal: String, target: String) {
        val current = _matchItState.value
        val newMatches = current.matches.toMutableMap()
        newMatches[animal] = target
        _matchItState.value = current.copy(matches = newMatches)
    }

    fun checkMatchAnswers() {
        if (_stats.value.livesCount <= 0) {
            showCelebration("¡Te has quedado sin vidas por hoy! Vuelve mañana para más vidas. ⏳")
            return
        }
        addXpAndPersist(80, 8, 0, 1, 1)
        showCelebration("¡Fantástico! ¡Todos los animales emparejados correctamente! 🐾")
    }

    private fun addXpAndPersist(xp: Int, gems: Int, streak: Int, lessons: Int, games: Int) {
        val current = _userEntity.value
        if (current != null) {
            viewModelScope.launch {
                userRepository.updateStats(current.username, xp, gems, streak, lessons, games)
            }
        }
    }

    fun showCelebration(message: String) {
        _celebrationText.value = message
    }

    fun dismissCelebration() {
        _celebrationText.value = null
    }
}
