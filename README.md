# App:점메추(Food Recommendations App)


## 소개

카테고리 선택 형식의 메뉴 추천 앱입니다. 
매운 음식과 맵지 않은 음식, 뜨거운 음식과 차가운 음식 등의 당일 원하는 느낌의 카테고리를 선택하면
해당 음식들을 한식/일식/중식/양식/아시안 순서로 나열됩니다.

![Food Recommendations App](https://github.com/user-attachments/assets/8906b7a0-10e2-4b1e-99af-7ca1269fb4ce)



| 분야 | 사용 기술 |
| --- | --- |
| **UI** | Jetpack Compose, Material3, AnimatedVisibility |
| **상태관리** | ViewModel, `mutableStateOf`, `StateFlow` |
| **데이터 저장** | Room (Memo DB), LocalDate 기반 날짜 처리 |
| **위치 기반** | FusedLocationProvider + Kakao Local API |
| **지도 연동** | WebView (Kakao 지도 링크) |
| **기타** | WebView, LazyColumn, FlowRow, 권한 처리 등 |


## 카테고리 선택


![카테고리 선택-결과(github)](https://github.com/user-attachments/assets/1f37a8c5-a342-4204-b5ed-2e5d00ab7dbb)

![Map](https://github.com/user-attachments/assets/8afd9116-c2cf-4c48-af39-48bae78983b2)



## 달력 메모


![달력 메모](https://github.com/user-attachments/assets/0ee34c7a-080a-4a38-a123-3b4c87497e86)

- 오른쪽 상단 달력 메모 기능
- *추가예정*
  - 어제~1주전 먹은 음식 추천 제외
  - 아침/점심/저녁 식단 분리


## 랜덤 선택


![랜덤 메뉴 추천(github)](https://github.com/user-attachments/assets/a72e3adc-4fba-45ad-a70e-9fee24c0547d)

- 추천 받은 데이터에서 무작위로 하나 추천(재시작 가능)

## 🧠 핵심 코드 설명

### 1. ✅ 조건 기반 추천 로직 (ViewModel)

```kotlin
private fun getMatchingConditions(): Set<String> {
    val selectedButtons = _buttonStates.value.filterValues { it }.keys
    if (selectedButtons.isEmpty()) return emptySet()
    val allMatching = FoodsData.foodsList
        .filter { food -> selectedButtons.all { it in food.tags.map { tag -> tag.title } } }
        .map { it.title }
        .toSet()
    return allMatching - _excludedFoods.value
}
```
선택된 태그(예: "매움", "면", "뜨거움")와 일치하는 음식만 필터링.
최근 먹은 음식은 자동으로 제외하여 추천 신뢰도를 높임.

### 2. 🗓 최근 며칠간 먹은 음식 제외 기능
```kotlin
fun updateExcludedFoods(daysAgo: Int) {
    viewModelScope.launch {
        if (daysAgo == 0) {
            _excludedFoods.value = emptySet()
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
            _excludedFoods.value = excluded
        }
        if (selectedMode.value == "모두 일치") updateMatchingConditions()
        else updateAnyMatchingConditions()
    }
}
```
음식 기록을 조회해 최근 먹은 항목을 자동으로 제외함.
Slider로 일 수 조절 가능.

### 3. 🎰 룰렛 화면 (기본 + 커스텀)
```kotlin
val rouletteList = if (isCustomMode)
    mainViewModel.customRouletteItems else mainViewModel.matchingConditions.value.toList()

val randomIndex by transition.animateValue(
    initialValue = 0,
    targetValue = (rouletteList.size - 1).coerceAtLeast(0),
    typeConverter = Int.VectorConverter,
    animationSpec = infiniteRepeatable(
        animation = tween(durationMillis = 100, easing = LinearEasing),
        repeatMode = RepeatMode.Restart
    ),
)
```
애니메이션 기반 룰렛 구성.
커스텀 모드에서는 사용자 입력 항목으로 구성 가능 ("누가 계산할까?", "오늘 회식 메뉴는?")

### 4. 🗺 Kakao Map 연동 (WebView 기반)
```kotlin
WebView(it).apply {
    webViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            if (url?.startsWith("intent://") == true) {
                try {
                    val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                    context.startActivity(intent)
                    return true
                } catch (e: Exception) { e.printStackTrace() }
            }
            return false
        }
    }
    settings.javaScriptEnabled = true
    loadUrl("https://map.kakao.com/?q=$conditionKey")
}
```
카카오 지도 검색 결과를 앱 내에서 바로 표시.
클릭 시 카카오맵 앱으로 연동됨.

