package eu.tutorials.jeommechu.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import eu.tutorials.jeommechu.data.foodsData

class MainViewModel : ViewModel() {
    // 조건과 일치하는 음식 목록을 저장하는 상태 변수
    private val _matchingConditions = mutableStateOf<Set<String>>(emptySet())
    val matchingConditions: State<Set<String>> = _matchingConditions

    // 선택된 조건을 저장하는 상태 변수
    private val _toggleConditions = mutableStateOf<Map<String, List<String>>>(emptyMap())
    val toggleConditions: State<Map<String, List<String>>> = _toggleConditions

    // 각 버튼의 선택 상태를 저장하는 상태 변수
    private val _buttonStates = mutableStateOf(mutableMapOf<String, Boolean>())
    val buttonStates: State<Map<String, Boolean>> = _buttonStates


    // 버튼 클릭 시 상태를 토글하는 함수
    fun toggleButton(button: String) {
        _buttonStates.value = _buttonStates.value.toMutableMap().apply {
            this[button] = !(this[button] ?: false)
        }
    }

    var selectedMode = mutableStateOf("모두 일치")
        private set

    // selectedMode 가 변경될 때 자동으로 적용할 함수
    fun setSelectedMode(mode: String) {
        selectedMode.value = mode
        when (mode) {
            "모두 일치" -> updateMatchingConditions()
            "하나라도 일치" -> updateAnyMatchingConditions()
        }
    }

    // --- 모두 일치 조건 (모든 선택 버튼이 포함) --- //

    // 현재 선택한 버튼들이 모두 포함된 음식 키들을 반환 ("모두 일치" 조건)
    fun getMatchingConditions(): Set<String> {
        val selectedButtons = _buttonStates.value.filterValues { it }.keys
        if (selectedButtons.isEmpty()) return emptySet()
        return foodsData.filter { (_, buttons) ->
            selectedButtons.all { it in buttons }
        }.keys
    }

    // 모두 일치 조건을 적용하여 상태를 업데이트
    fun updateMatchingConditions() {
        val newMatchingConditions = getMatchingConditions()
        _matchingConditions.value = newMatchingConditions
        _toggleConditions.value = getMatchingConditionsMap(newMatchingConditions)
    }

    // --- 하나라도 일치 조건 (선택한 버튼 중 하나라도 포함 되면 됨) --- //

    // 현재 선택한 버튼 중 하나라도 포함된 음식 키들을 반환 ("하나라도 일치" 조건)
    fun getAnyMatchingConditions(): Set<String> {
        val selectedButtons = _buttonStates.value.filterValues { it }.keys
        if (selectedButtons.isEmpty()) return emptySet()
        return foodsData.filter { (_, buttons) ->
            selectedButtons.any { it in buttons }
        }.keys
    }

    // 하나라도 일치 조건을 적용하여 상태를 업데이트
    fun updateAnyMatchingConditions() {
        val newMatchingConditions = getAnyMatchingConditions()
        _matchingConditions.value = newMatchingConditions
        _toggleConditions.value = getMatchingConditionsMap(newMatchingConditions)
    }

    // 주어진 음식 키들에 대해, 각 음식 키에 해당하는 태그 목록을 매핑하여 반환
    fun getMatchingConditionsMap(conditions: Set<String>): Map<String, List<String>> {
        return conditions.associateWith { foodsData[it] ?: emptyList() }
    }

    var selectedCondition by mutableStateOf("❓")
        private set

    fun updateSelectedCondition(newValue: String) {
        selectedCondition = newValue
    }
}