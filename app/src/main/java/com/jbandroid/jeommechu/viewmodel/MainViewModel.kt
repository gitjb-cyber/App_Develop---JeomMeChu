package com.jbandroid.jeommechu.viewmodel

import android.R.attr.label
import android.app.Application
import android.location.Location
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jbandroid.jeommechu.calendar_memo_db.Memo
import com.jbandroid.jeommechu.calendar_memo_db.MemoDatabase
import com.jbandroid.jeommechu.calendar_memo_db.MemoRepository
import com.jbandroid.jeommechu.kakaoMap.KakaoRepository
import com.jbandroid.jeommechu.kakaoMap.PlaceModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import com.jbandroid.jeommechu.data.Tag
import com.jbandroid.jeommechu.data.Tags
import com.jbandroid.jeommechu.data.Food
import com.jbandroid.jeommechu.data.FoodsData


class MainViewModel(application: Application) : AndroidViewModel(application) {
    // 조건과 일치하는 음식 제목들을 저장하는 상태 변수
    private val _matchingConditions = mutableStateOf<Set<String>>(emptySet())
    val matchingConditions: State<Set<String>> = _matchingConditions

    // 각 음식 제목에 해당하는 태그 목록을 저장하는 상태 변수
    private val _toggleConditions = mutableStateOf<Map<String, List<String>>>(emptyMap())
    val toggleConditions: State<Map<String, List<String>>> = _toggleConditions

    // 각 버튼(옵션)의 선택 상태를 저장하는 상태 변수
    private val _buttonStates =  mutableStateOf(mutableMapOf<String, Boolean>())
    val buttonStates: State<Map<String, Boolean>> = _buttonStates

    // 제외할 음식 상태 저장하는 상태 변수
    private val excludedFoods = mutableStateOf(setOf<String>())

    // 슬라이더에 사용할 상태 변수
    private val _sliderDaysAgo = mutableStateOf(0)
    val sliderDaysAgo: State<Int> = _sliderDaysAgo


    // 랜덤 메뉴 값을 저장하는 상태 변수
    var selectedCondition by mutableStateOf("❓")

    // 커스텀 룰렛 리스트를 저장하는 상태 변수
    private val _customRouletteItems = mutableStateListOf<String>()
    val customRouletteItems: List<String> get() = _customRouletteItems

    // 메모 Database 저장 변수
    private val memoDao = MemoDatabase.getDatabase(application).memoDao()
    private val repository = MemoRepository(memoDao)

    val memoList: StateFlow<List<Memo>> = repository.allMemos
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())



    // 모든 음식 title 넣기
    fun setMatchingConditionsFromAllFoods() {
        val allTitles = FoodsData.foodsList.map { it.title }.toSet()
        _matchingConditions.value = allTitles
        _toggleConditions.value = getMatchingConditionsMap(allTitles)
    }

    // 감정 선택
    enum class Emotion(val label: String) {
        FAST_ONE("빠르게 한끼"),
        LIGHT_ONE("가볍게 1인분"),
        COLD("차가운 음식이 땡겨요"),
        SPICY("얼큰하게 스트레스 풀고 싶어요"),
        HEARTY("든든하게 배 채우고 싶어요");

        companion object {
            fun fromLabel(text: String): Emotion? =
                entries.firstOrNull { it.label == text }
        }
    }


    // SelectionResult에서 쓰면 label로 계속 보관
    var currentEmotionLabel by mutableStateOf<String?>(null)

    // Enum 자체도 보관
    var currentEmotion: Emotion? by mutableStateOf(null)

    // 유틸
    private fun Food.has(vararg tags: Tag) = tags.any { it in this.tags }
    private fun Food.hasAny(tags: Collection<Tag>) = tags.any { it in this.tags }

    private fun candidatesByEmotion(e: Emotion): List<Food> {
        val meats = listOf(Tags.Pork, Tags.Beef, Tags.Chicken, Tags.Lamb)
        return when (e) {
            Emotion.FAST_ONE -> FoodsData.foodsList.filter { it.has(Tags.Simple)}
            Emotion.LIGHT_ONE -> FoodsData.foodsList.filter { it.has(Tags.OneBowl) }
            Emotion.COLD -> FoodsData.foodsList.filter { it.has(Tags.Cold) }
            Emotion.SPICY -> FoodsData.foodsList.filter { it.has(Tags.Spicy) }
            Emotion.HEARTY -> FoodsData.foodsList.filter { food ->
                val warmish = food.has(Tags.Hot) || food.has(Tags.Warm)
                val hasMeat = food.hasAny(meats)
                warmish && hasMeat
            }
        }
    }

    // 결과 화면 등에서 후보 전부 필요할 때
    fun getFoodsByEmotion(e: Emotion): List<String> = candidatesByEmotion(e).map { it.title }

    fun getFoodsByEmotion(label: String?): List<String> =
        label?.let { Emotion.fromLabel(it) }?.let { getFoodsByEmotion(it) } ?: emptyList()

    // 감정 선택 시 랜덤 음식 추천 함수
    fun selectFoodByEmotion(e: Emotion): String {
        val list = candidatesByEmotion(e)
        currentEmotion = e
        currentEmotionLabel = e.label
        return list.randomOrNull()?.title ?: "❓"
    }
    fun selectFoodByEmotion(label: String): String =
        Emotion.fromLabel(label)?.let { selectFoodByEmotion(it) } ?: "❓"

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
        }
    }


    // memo에 저장된 음식 중 최근 n일간의 음식
    private fun updateExcludedFoods(daysAgo: Int) {
        viewModelScope.launch {
            if (daysAgo == 0) {
                excludedFoods.value = emptySet()
            } else {
                val today = LocalDate.now()
                val targetDates = (1..daysAgo).map { today.minusDays(it.toLong()).toString() }

                val memos = repository.allMemos.first()
                val excluded = memos
                    .asSequence()
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

    // memo에 저장된 음식 중 최근 n일간의 음식 제외
    fun setSliderDaysAgo(days: Int) {
        _sliderDaysAgo.value = days
        updateExcludedFoods(days)
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

    // 위치 상태
    private val _location = MutableStateFlow<Location?>(null)
    val location: StateFlow<Location?> = _location

    fun updateLocation(newLocation: Location?) {
        _location.value = newLocation
    }

    // 반경 설정 (km)
    private val _searchRadius = MutableStateFlow(1.0f)
    val searchRadius: StateFlow<Float> = _searchRadius

    fun setSearchRadius(value: Float) {
        _searchRadius.value = value
        filterAndSortPlaces()
    }

    // 오류 메시지
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun setError(message: String) {
        _error.value = message
    }

    // 전체 장소 리스트 (가공 전)
    private var rawPlaces: List<PlaceModel> = emptyList()

    // 화면에 보여줄 장소 리스트 (필터+정렬 적용)
    private val _places = MutableStateFlow<List<PlaceModel>>(emptyList())
    val places: StateFlow<List<PlaceModel>> = _places

    // 카카오맵 장소 검색
    private val kakaoRepository = KakaoRepository()
    fun searchNearbyPlaces(keyword: String) {
        viewModelScope.launch {
            val current = _location.value
            if (current == null) {
                setError("위치를 가져올 수 없습니다.")
                return@launch
            }

            try {
                val x = current.longitude
                val y = current.latitude

                val documents = kakaoRepository.searchNearbyPlaces(keyword, x, y)

                rawPlaces = documents.map {
                    PlaceModel(
                        placeName = it.placeName,
                        distance = it.distance.toDoubleOrNull() ?: 0.0,
                        address = it.addressName,
                        latitude = it.latitude,
                        longitude = it.longitude
                    )
                }

                _error.value = null
                filterAndSortPlaces()
            } catch (e: Exception) {
                _error.value = "장소 검색 중 오류 발생: ${e.localizedMessage}"
            }
        }
    }

    // 필터링 + 정렬 적용 함수
    private fun filterAndSortPlaces() {
        val filtered = rawPlaces.filter {
            it.distance <= (_searchRadius.value * 1000).toInt()
        }

        val sorted = filtered.sortedBy { it.distance }

        _places.value = sorted
    }

    // 현재 위치 주소 저장
    private val _currentAddress = MutableStateFlow<String?>(null)
    val currentAddress: StateFlow<String?> = _currentAddress

    fun fetchCurrentAddress() {
        viewModelScope.launch {
            val loc = _location.value ?: return@launch
            val address = kakaoRepository.getAddressFromCoordinates(loc.longitude, loc.latitude)
            _currentAddress.value = address
        }
    }
}

enum class SelectMode(val label: String) {
    ALL_MATCH("모두 일치");

    companion object {
        fun fromLabel(label: String): SelectMode? =
            entries.find { it.label == label }
    }
}
