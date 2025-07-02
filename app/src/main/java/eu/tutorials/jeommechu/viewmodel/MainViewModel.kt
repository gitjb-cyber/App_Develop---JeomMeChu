package eu.tutorials.jeommechu.viewmodel

import android.app.Application
import android.content.Context
import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import eu.tutorials.jeommechu.calendar_memo_db.Memo
import eu.tutorials.jeommechu.calendar_memo_db.MemoDatabase
import eu.tutorials.jeommechu.calendar_memo_db.MemoRepository
import eu.tutorials.jeommechu.data.FoodsData
import eu.tutorials.jeommechu.kakaoMap.KakaoRepository
import eu.tutorials.jeommechu.kakaoMap.PlaceDocument
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate


class MainViewModel(application: Application) : AndroidViewModel(application) {
    // 조건과 일치하는 음식 제목들을 저장하는 상태 변수
    private val _matchingConditions = mutableStateOf<Set<String>>(emptySet())
    val matchingConditions: State<Set<String>> = _matchingConditions

    // 각 음식 제목에 해당하는 태그 목록을 저장하는 상태 변수
    private val _toggleConditions = mutableStateOf<Map<String, List<String>>>(emptyMap())
    val toggleConditions: State<Map<String, List<String>>> = _toggleConditions

    // 각 버튼(옵션)의 선택 상태를 저장하는 상태 변수
    private val _buttonStates = mutableStateOf(mutableMapOf<String, Boolean>())
    val buttonStates: State<Map<String, Boolean>> = _buttonStates

    // 제외할 음식 상태 저장하는 상태 변수
    private val excludedFoods = mutableStateOf(setOf<String>())

    // 슬라이더에 사용할 상태 변수
    private val _sliderDaysAgo = mutableStateOf(0)
    val sliderDaysAgo: State<Int> = _sliderDaysAgo


    // 랜덤 메뉴 값을 저장하는 상태 변수
    private var selectedCondition by mutableStateOf("❓")

    // 커스텀 룰렛 리스트를 저장하는 상태 변수
    private val _customRouletteItems = mutableStateListOf<String>()
    val customRouletteItems: List<String> get() = _customRouletteItems

    // 메모 Database 저장 변수
    private val memoDao = MemoDatabase.getDatabase(application).memoDao()
    private val repository = MemoRepository(memoDao)

    val memoList: StateFlow<List<Memo>> = repository.allMemos
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    // 랜덤 추천에서 사용할 데이터값을 변경하는 함수(추천된 데이터로 업데이트)
    fun updateSelectedCondition(newValue: String) {
        selectedCondition = newValue
    }

    // 커스텀 룰렛 설정 함수(아이템 추가)
    fun addCustomItem(item: String) {
        if (item.isNotBlank() && !_customRouletteItems.contains(item)) {
            _customRouletteItems.add(item)
        }
    }

    // 커스텀 룰렛 설정 함수(아이템 삭제)
    fun removeCustomItem(item: String) {
        _customRouletteItems.remove(item)
    }


    // 버튼 클릭 시 상태를 토글하는 함수
    fun toggleButton(button: String) {
        _buttonStates.value = _buttonStates.value.toMutableMap().apply {
            this[button] = !(this[button] ?: false)
        }
    }

    // 전체 선택 -> 다른 버튼의 상태를 강제 설정하는 함수
    fun setButtonState(button: String, isChecked: Boolean) {
        _buttonStates.value = _buttonStates.value.toMutableMap().apply {
            this[button] = isChecked
        }
    }

    // RecommendationScreen 에서 보여주는 기본 값
    var selectedMode = mutableStateOf(SelectMode.ALL_MATCH)
        private set

    // selectedMode가 변경될 때 호출되는 함수
    fun setSelectedMode(mode: SelectMode) {
        selectedMode.value = mode
        when (mode) {
            SelectMode.ALL_MATCH -> updateMatchingConditions()
            SelectMode.ANY_MATCH -> updateAnyMatchingConditions()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setSliderDaysAgo(days: Int) {
        _sliderDaysAgo.value = days
        updateExcludedFoods(days)
    }

    // memo에 저장된 음식 중 최근 n일간의 음식 제외
    @RequiresApi(Build.VERSION_CODES.O)
    fun updateExcludedFoods(daysAgo: Int) {
        viewModelScope.launch {
            if (daysAgo == 0) {
                excludedFoods.value = emptySet()
            } else {
                val today = LocalDate.now()
                val targetDates = (1..daysAgo).map { today.minusDays(it.toLong()).toString() }

                val memos = repository.allMemos.first()
                val excluded = memos
                    .filter { it.date in targetDates }
                    .flatMap { it.memo.split(",", " ") }
                    .map { it.trim() }
                    .filter { it.isNotBlank() }
                    .toSet()

                excludedFoods.value = excluded
            }
            // 조건 다시 계산
            if (selectedMode.value == SelectMode.ALL_MATCH) {
                updateMatchingConditions()
            } else {
                updateAnyMatchingConditions()
            }
        }
    }

    // --- 모두 일치 조건 (모든 선택 버튼이 포함) --- //
    private fun getMatchingConditions(): Set<String> {
        val selectedButtons = _buttonStates.value.filterValues { it }.keys
        if (selectedButtons.isEmpty()) return emptySet()
        val allMatching = FoodsData.foodsList
            .filter { food ->
                selectedButtons.all { option ->
                    option in food.tags.map { it.title }
                }
            }
            .map { it.title }
            .toSet()
        return allMatching - excludedFoods.value
    }

    // 카테고리에서 일치 조건을 충족시킨 음식 모음(교집합)
    fun updateMatchingConditions() {
        val newMatchingConditions = getMatchingConditions()
        _matchingConditions.value = newMatchingConditions
        _toggleConditions.value = getMatchingConditionsMap(newMatchingConditions)
    }

    // --- 하나라도 일치 조건 (선택한 버튼 중 하나라도 포함) --- //
    private fun getAnyMatchingConditions(): Set<String> {
        val selectedButtons = _buttonStates.value.filterValues { it }.keys
        if (selectedButtons.isEmpty()) return emptySet()
        val allMatchingAny = FoodsData.foodsList
            .filter { food ->
                selectedButtons.any { option ->
                    option in food.tags.map { it.title }
                }
            }
            .map { it.title }
            .toSet()
        return allMatchingAny - excludedFoods.value
    }

    // 카테고리에서 일치 조건을 충족시킨 음식 모음(합집합)
    private fun updateAnyMatchingConditions() {
        val newMatchingConditions = getAnyMatchingConditions()
        _matchingConditions.value = newMatchingConditions
        _toggleConditions.value = getMatchingConditionsMap(newMatchingConditions)
    }

    // 주어진 음식 제목들에 대해, 해당 음식의 태그 목록을 매핑하여 반환
    private fun getMatchingConditionsMap(conditions: Set<String>): Map<String, List<String>> {
        return FoodsData.foodsList.filter { it.title in conditions }
            .associate { it.title to it.tags.map { tag -> tag.title } }
    }


    // 날짜 - 음식 메모 DB 저장
    fun insertMemo(date: String, memoText: String) {
        viewModelScope.launch {
            repository.insert(Memo(date, memoText))
        }
    }

    // 저장된 메모 DB 삭제
    fun deleteMemo(memo: Memo) {
        viewModelScope.launch {
            repository.delete(memo)
        }
    }

    // 카카오 맵
    private val location = mutableStateOf<Location?>(null)
    private var locationJob: Job? = null

    fun updateLocation(newLocation: Location?) {
        location.value = newLocation
    }

    private val kakaoRepository = KakaoRepository()

    private val _places = MutableStateFlow<List<PlaceDocument>>(emptyList())
    val places: StateFlow<List<PlaceDocument>> = _places

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun searchNearbyPlaces(conditionKey: String) {
        viewModelScope.launch {
            val current = location.value
            if (current == null) {
                _error.value = "위치를 가져올 수 없습니다."
                return@launch
            }

            try {
                val x = current.longitude
                val y = current.latitude
                val results = kakaoRepository.searchNearbyPlaces(conditionKey, x, y)
                _places.value = results
                _error.value = null // 성공 시 에러 초기화
            } catch (e: Exception) {
                _error.value = "장소 검색 중 오류 발생"
            }
        }
    }

    fun startLocationUpdates(context: Context, conditionKey: String) {
        stopLocationUpdates() // 기존 작업 중지

        val fusedClient = LocationServices.getFusedLocationProviderClient(context)

        locationJob = viewModelScope.launch {
            while (isActive) {
                try {
                    val location = fusedClient
                        .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                        .await()
                    updateLocation(location)
                    searchNearbyPlaces(conditionKey)
                } catch (e: SecurityException) {
                    _error.value = "위치 권한이 필요합니다."
                    updateLocation(null)
                } catch (e: Exception) {
                    _error.value = "위치 갱신 실패: ${e.message}"
                }
                delay(10000) // 10초마다 위치 재요청
            }
        }
    }

    fun stopLocationUpdates() {
        locationJob?.cancel()
        locationJob = null
    }

    fun setError(message: String) {
        _error.value = message
    }

}

enum class SelectMode(val label: String) {
    ALL_MATCH("모두 일치"),
    ANY_MATCH("하나라도 일치");

    companion object {
        fun fromLabel(label: String): SelectMode? =
            entries.find { it.label == label }
    }
}