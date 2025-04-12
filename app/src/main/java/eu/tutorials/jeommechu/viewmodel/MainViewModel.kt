package eu.tutorials.jeommechu.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import eu.tutorials.jeommechu.data.FoodsData

@RequiresApi(Build.VERSION_CODES.O)
class MainViewModel() : ViewModel() {
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
    var selectedCondition by mutableStateOf("❓")
        private set
/*

    // 선택한 날짜
    var selectedDate by mutableStateOf(LocalDate.now().toString())
        private set

    // selectedDate 의 메모
    var currentMemo by mutableStateOf("")
        private set

    // 모든 메모를 Map 형태로 관리 (날짜 -> 메모)
    var memoMap by mutableStateOf<Map<String, String>>(emptyMap())
        private set

    // lateinit : 늦은 초기화. 결과값을 기반으로 getAllWish 초기화(값이 계속해서 바뀔 수 있다)(val 사용 X)
    // 초기화가 확실하지 않거나 해당 속성에 접근할 때마다 null 체크를 하고 싶지 않을 때 사용
    // Flow 사용은 비동기 상태
    lateinit var getAllMemos: Flow<List<Memo>>
*/

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
    fun getMatchingConditions(): Set<String> {
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
    fun getAnyMatchingConditions(): Set<String> {
        val selectedButtons = _buttonStates.value.filterValues { it }.keys
        if (selectedButtons.isEmpty()) return emptySet()
        return FoodsData.foodsList.filter { food ->
            selectedButtons.any { option ->
                option in food.tags.map { it.title }
            }
        }.map { it.title }.toSet()
    }

    fun updateAnyMatchingConditions() {
        val newMatchingConditions = getAnyMatchingConditions()
        _matchingConditions.value = newMatchingConditions
        _toggleConditions.value = getMatchingConditionsMap(newMatchingConditions)
    }

    // 주어진 음식 제목들에 대해, 해당 음식의 태그 목록을 매핑하여 반환
    fun getMatchingConditionsMap(conditions: Set<String>): Map<String, List<String>> {
        return FoodsData.foodsList.filter { it.title in conditions }
            .associate { it.title to it.tags.map { tag -> tag.title } }
    }

/*
    init {
        // Repository로부터 모든 메모 변경사항을 수집하여 상태 업데이트
        viewModelScope.launch {
            getAllMemos = memoRepository.getAllMemos()
        }
    }

    fun addMemo(memo: Memo){
        viewModelScope.launch(Dispatchers.IO) {
            memoRepository.addMemo(memo = memo)
        }
    }

    fun updateMemo(memo: Memo) {
        viewModelScope.launch(Dispatchers.IO) {
            memoRepository.updateAMemo(memo = memo)
        }
    }

    fun deleteMemo(memo: Memo) {
        viewModelScope.launch(Dispatchers.IO) {
            memoRepository.deleteAMemo(memo = memo)
        }
    }

    // 날짜 선택 변경 시 호출(selectedDate, currentMemo 업데이트)
    fun updateSelectedDate(date: String) {
        selectedDate = date
        currentMemo = memoMap[date] ?: ""
    }

    // 메모 텍스트가 변경될 때 상태 업데이트
    fun onMemoChange(memoText: String) {
        currentMemo = memoText
    }

    // 저장 버튼 클릭 시 호출되는 메소드
    fun saveCurrentMemo() {
        val memo = Memo(date = selectedDate, memo = currentMemo)
        viewModelScope.launch {
            memoRepository.addMemo(memo)
        }
    }*/

}