package eu.tutorials.jeommechu.screen_view

sealed class ScreenRoute(val route: String)
{
    object StartScreen : ScreenRoute("startScreen") // 어플을 켰을 때 첫 번째 화면
    object SelectionScreen : ScreenRoute("selectionScreen") // 음식 선택 화면
    object RecommendationScreen : ScreenRoute("recommendationScreen") // 음식 추천 화면
    object UserMap : ScreenRoute("userMap") // 사용자 주변 음식점
    object RouletteScreen : ScreenRoute("roulette") // 룰렛 화면
    object CalendarMemoScreen : ScreenRoute("calendarMemoScreen") // 달력 화면

}