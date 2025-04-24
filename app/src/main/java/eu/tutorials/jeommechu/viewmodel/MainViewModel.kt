package eu.tutorials.jeommechu.viewmodel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import eu.tutorials.jeommechu.BuildConfig
import eu.tutorials.jeommechu.calendar_memo_db.Memo
import eu.tutorials.jeommechu.calendar_memo_db.MemoDatabase
import eu.tutorials.jeommechu.calendar_memo_db.MemoRepository
import eu.tutorials.jeommechu.data.FoodsData
import eu.tutorials.jeommechu.kakaoMap.KakaoRepository
import eu.tutorials.jeommechu.kakaoMap.PlaceDocument
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@RequiresApi(Build.VERSION_CODES.O)
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

    // 랜덤 메뉴 돌리기 전 나오는 기본 텍스트
    private var selectedCondition by mutableStateOf("❓")

    // 메모 Database 저장 변수
    private val memoDao = MemoDatabase.getDatabase(application).memoDao()
    private val repository = MemoRepository(memoDao)

    val memoList: StateFlow<List<Memo>> = repository.allMemos
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    // 랜덤 추천에서 사용할 데이터(추천된 데이터로 업데이트)
    fun updateSelectedCondition(newValue: String) {
        selectedCondition = newValue
    }


    // 버튼 클릭 시 상태를 토글하는 함수
    fun toggleButton(button: String) {
        _buttonStates.value = _buttonStates.value.toMutableMap().apply {
            this[button] = !(this[button] ?: false)
        }
    }

    var selectedMode = mutableStateOf("모두 일치")
        private set

    // selectedMode가 변경될 때 호출되는 함수
    fun setSelectedMode(mode: String) {
        selectedMode.value = mode
        when (mode) {
            "모두 일치" -> updateMatchingConditions()
            "하나라도 일치" -> updateAnyMatchingConditions()
        }
    }

    // --- 모두 일치 조건 (모든 선택 버튼이 포함) --- //
    private fun getMatchingConditions(): Set<String> {
        val selectedButtons = _buttonStates.value.filterValues { it }.keys
        if (selectedButtons.isEmpty()) return emptySet()
        return FoodsData.foodsList.filter { food ->
            selectedButtons.all { option ->
                option in food.tags.map { it.title }
            }
        }.map { it.title }.toSet()
    }

    fun updateMatchingConditions() {
        val newMatchingConditions = getMatchingConditions()
        _matchingConditions.value = newMatchingConditions
        _toggleConditions.value = getMatchingConditionsMap(newMatchingConditions)
    }

    // --- 하나라도 일치 조건 (선택한 버튼 중 하나라도 포함) --- //
    private fun getAnyMatchingConditions(): Set<String> {
        val selectedButtons = _buttonStates.value.filterValues { it }.keys
        if (selectedButtons.isEmpty()) return emptySet()
        return FoodsData.foodsList.filter { food ->
            selectedButtons.any { option ->
                option in food.tags.map { it.title }
            }
        }.map { it.title }.toSet()
    }

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


    fun insertMemo(date: String, memoText: String) {
        viewModelScope.launch {
            repository.insert(Memo(date, memoText))
        }
    }


    // 카카오 맵
    private val kakaoRepository = KakaoRepository(BuildConfig.KAKAO_REST_API_KEY)
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)

    private val _places = MutableStateFlow<List<PlaceDocument>>(emptyList())
    val places: StateFlow<List<PlaceDocument>> = _places

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun searchNearbyPlaces(conditionKey: String) {
        viewModelScope.launch {
            try {
                val location = fusedLocationClient
                    .getCurrentLocation(
                        com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY,
                        null
                    )
                    .await()

                val x = location.longitude
                val y = location.latitude
                val results = kakaoRepository.searchNearbyPlaces(conditionKey, x, y)
                _places.value = results
            } catch (e: SecurityException) {
                _error.value = "위치 권한이 필요합니다."
            } catch (e: Exception) {
                _error.value = "장소 검색 중 오류 발생"
            }
        }
    }
}