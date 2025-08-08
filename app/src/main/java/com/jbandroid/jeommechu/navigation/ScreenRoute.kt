package com.jbandroid.jeommechu.navigation

sealed class ScreenRoute(val route: String)
{
    data object StartScreen : ScreenRoute("startScreen") // 어플을 켰을 때 첫 번째 화면
    // 음식 선택 화면
    data object SelectionIntro : ScreenRoute("selectionIntro")
    data object SelectionEmotion : ScreenRoute("selectionEmotion")
    data object SelectionResult : ScreenRoute("selectionResult")
    data object SelectionType : ScreenRoute("selectionType")
    data object SelectionFeature : ScreenRoute("selectionFeature")
    data object SelectionStyle : ScreenRoute("selectionStyle")
    data object RecommendationScreen : ScreenRoute("recommendationScreen") // 음식 추천 화면
    data object UserMap : ScreenRoute("userMap/{conditionKey}") {
        fun createRoute(conditionKey: String) = "userMap/$conditionKey"
    } // 사용자 주변 음식점
    data object RouletteScreen : ScreenRoute("roulette") // 룰렛 화면
    data object CalendarMemoScreen : ScreenRoute("calendarMemoScreen") // 메모 화면

}