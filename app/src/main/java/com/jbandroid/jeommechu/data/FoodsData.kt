package com.jbandroid.jeommechu.data

data class Tag(val title: String)

object Tags {
    // 탄수화물
    val Rice = Tag("밥🍚")
    val Bread = Tag("빵🍔")
    val Noodle = Tag("면🍝")
    val Tteok = Tag("떡")
    val Etc = Tag("기타")


    val SoupYes = Tag("국물⭕")
    val SoupNo = Tag("국물❌")

    val Hot = Tag("뜨거움🔥")
    val Warm = Tag("따뜻함")
    val Cold = Tag("차가움❄")

    val Mild = Tag("안매움")
    val Slightly_Spicy = Tag("살짝 매움")
    val Spicy = Tag("매움🥵")

    // 재료
    val Pork = Tag("돼지고기")
    val Beef = Tag("소고기")
    val Chicken = Tag("닭고기")
    val Fish = Tag("생선")
    val Seafood = Tag("해산물")
    val Egg = Tag("계란")
    val Vegan = Tag("비건")
    val Diet = Tag("다이어트식")
    val FishCake = Tag("(어묵)")
    val Lamb = Tag("양고기")

    // 나라별 카테고리
    val Korean = Tag("한식")
    val Japanese = Tag("일식")
    val Chinese = Tag("중식")
    val Western = Tag("양식")
    val Asian = Tag("아시안")

    // 식사 테그
    val Simple = Tag("간편 식사")
    val OneBowl = Tag("한 그릇 식사")

}

data class Food(val title: String, val tags: List<Tag>)

fun food(title: String, vararg tags: Tag): Food = Food(title, tags.toList())

// 음식 데이터와 해당하는 태그 목록을 저장한 맵
object FoodsData {
    val foodsList: List<Food> = listOf(

        // 한식
        food("백반", Tags.Korean, Tags.Rice, Tags.SoupYes, Tags.Hot, Tags.Mild, Tags.Vegan, Tags.Pork, Tags.Fish, Tags.FishCake, Tags.Egg),
        food("불고기", Tags.Korean, Tags.Rice, Tags.SoupYes, Tags.Hot, Tags.Mild, Tags.Pork),
        food("돼지고기 김치찌개", Tags.Korean, Tags.Rice, Tags.SoupYes, Tags.Hot, Tags.Slightly_Spicy, Tags.Pork),
        food("참치 김치찌개", Tags.Korean, Tags.Rice, Tags.SoupYes, Tags.Hot, Tags.Slightly_Spicy, Tags.Fish),
        food("스팸 김치찌개", Tags.Korean, Tags.Rice, Tags.SoupYes, Tags.Hot, Tags.Slightly_Spicy, Tags.Pork),
        food("미역국", Tags.Korean, Tags.Rice, Tags.SoupYes, Tags.Hot, Tags.Mild, Tags.Beef),
        food("삼계탕", Tags.Korean, Tags.Rice, Tags.SoupYes, Tags.Hot, Tags.Mild, Tags.Chicken),
        food("닭한마리", Tags.Korean, Tags.Rice, Tags.Noodle, Tags.SoupYes, Tags.Hot, Tags.Mild, Tags.Chicken),
        food("된장찌개", Tags.Korean, Tags.Rice, Tags.SoupYes, Tags.Hot, Tags.Mild, Tags.Pork),
        food("부대찌개", Tags.Korean, Tags.Rice, Tags.SoupYes, Tags.Hot, Tags.Slightly_Spicy, Tags.Pork),
        food("뼈해장국", Tags.Korean, Tags.Rice, Tags.SoupYes, Tags.Hot, Tags.Slightly_Spicy, Tags.Pork),
        food("갈비탕", Tags.Korean, Tags.Rice, Tags.SoupYes, Tags.Hot, Tags.Mild, Tags.Pork),
        food("선지 해장국", Tags.Korean, Tags.Rice, Tags.SoupYes, Tags.Hot, Tags.Mild, Tags.Pork),
        food("우거지 해장국", Tags.Korean, Tags.Rice, Tags.SoupYes, Tags.Hot, Tags.Mild, Tags.Vegan),
        food("육개장", Tags.Korean, Tags.Rice, Tags.SoupYes, Tags.Hot, Tags.Spicy, Tags.Pork),
        food("순대국밥", Tags.Korean, Tags.OneBowl, Tags.Rice, Tags.SoupYes, Tags.Hot, Tags.Mild, Tags.Pork),
        food("얼큰 순대국밥", Tags.Korean, Tags.OneBowl, Tags.Rice, Tags.SoupYes, Tags.Hot, Tags.Spicy, Tags.Pork),
        food("순두부찌개", Tags.Korean, Tags.Rice, Tags.SoupYes, Tags.Hot, Tags.Spicy, Tags.Seafood),
        food("돼지국밥", Tags.Korean, Tags.OneBowl, Tags.Rice, Tags.SoupYes, Tags.Hot, Tags.Mild, Tags.Pork),
        food("콩나물국밥", Tags.Korean, Tags.OneBowl, Tags.Rice, Tags.SoupYes, Tags.Hot, Tags.Mild, Tags.Egg),
        food("얼큰 콩나물국밥", Tags.Korean, Tags.OneBowl, Tags.Rice, Tags.SoupYes, Tags.Hot, Tags.Spicy, Tags.Egg),
        food("소내장탕", Tags.Korean, Tags.Rice, Tags.SoupYes, Tags.Hot, Tags.Spicy, Tags.Beef),
        food("닭볶음탕", Tags.Korean, Tags.Rice, Tags.SoupYes, Tags.Hot, Tags.Slightly_Spicy, Tags.Chicken),
        food("감자탕", Tags.Korean, Tags.Rice, Tags.SoupYes, Tags.Hot, Tags.Slightly_Spicy, Tags.Pork),
        food("곱창전골", Tags.Korean, Tags.Rice, Tags.SoupYes, Tags.Hot, Tags.Slightly_Spicy, Tags.Pork),
        food("곱도리탕", Tags.Korean, Tags.Rice, Tags.SoupYes, Tags.Hot, Tags.Slightly_Spicy, Tags.Pork),
        food("알탕", Tags.Korean, Tags.Rice, Tags.SoupYes, Tags.Hot, Tags.Slightly_Spicy, Tags.Fish, Tags.Seafood),
        food("수제비", Tags.Korean, Tags.Noodle, Tags.SoupYes, Tags.Hot, Tags.Mild, Tags.Seafood),
        food("김치 수제비", Tags.Korean, Tags.Noodle, Tags.SoupYes, Tags.Hot, Tags.Slightly_Spicy, Tags.Seafood),
        food("칼국수", Tags.Korean, Tags.Noodle, Tags.SoupYes, Tags.Hot, Tags.Mild, Tags.Seafood),
        food("라면", Tags.Korean, Tags.Simple, Tags.Noodle, Tags.SoupYes, Tags.Hot, Tags.Slightly_Spicy, Tags.Egg),
        food("잔치국수", Tags.Korean, Tags.Noodle, Tags.SoupYes, Tags.Hot, Tags.Mild, Tags.Seafood),
        food("떡국", Tags.Korean, Tags.Tteok, Tags.SoupYes, Tags.Hot, Tags.Mild, Tags.Egg),
        food("떡볶이", Tags.Korean, Tags.Simple, Tags.Tteok, Tags.SoupYes, Tags.Hot, Tags.Spicy, Tags.Fish, Tags.FishCake),
        food("즉석 떡볶이", Tags.Korean, Tags.Tteok, Tags.SoupYes, Tags.Hot, Tags.Spicy, Tags.Fish, Tags.FishCake),
        food("로제 떡볶이", Tags.Korean, Tags.Tteok, Tags.SoupYes, Tags.Hot, Tags.Spicy, Tags.Fish, Tags.FishCake),
        food("냉면", Tags.Korean, Tags.Noodle, Tags.SoupYes, Tags.Cold, Tags.Mild, Tags.Pork),
        food("밀면", Tags.Korean, Tags.Noodle, Tags.SoupYes, Tags.Cold, Tags.Mild, Tags.Pork),
        food("생선구이", Tags.Korean, Tags.Rice, Tags.SoupNo, Tags.Warm, Tags.Mild, Tags.Fish),
        food("스팸 볶음밥", Tags.Korean, Tags.OneBowl, Tags.Rice, Tags.SoupNo, Tags.Hot, Tags.Mild, Tags.Pork),
        food("김치 볶음밥", Tags.Korean, Tags.OneBowl, Tags.Rice, Tags.SoupNo, Tags.Hot, Tags.Slightly_Spicy, Tags.Egg),
        food("아귀찜", Tags.Korean, Tags.Rice, Tags.SoupNo, Tags.Hot, Tags.Slightly_Spicy, Tags.Fish),
        food("낚지덮밥", Tags.Korean, Tags.Rice, Tags.OneBowl, Tags.SoupNo, Tags.Hot, Tags.Spicy, Tags.Seafood),
        food("쭈꾸미 볶음", Tags.Korean, Tags.Rice, Tags.SoupNo, Tags.Hot, Tags.Spicy, Tags.Seafood),
        food("쭈꾸미 삼겹살", Tags.Korean, Tags.Rice, Tags.SoupNo, Tags.Hot, Tags.Spicy, Tags.Seafood, Tags.Pork),
        food("닭갈비", Tags.Korean, Tags.Rice, Tags.SoupNo, Tags.Hot, Tags.Spicy, Tags.Chicken),
        food("삼겹살 구이", Tags.Korean, Tags.Etc, Tags.SoupNo, Tags.Hot, Tags.Mild, Tags.Pork),
        food("곱창 구이", Tags.Korean, Tags.Etc, Tags.SoupNo, Tags.Hot, Tags.Mild, Tags.Pork),
        food("제육 덮밥", Tags.Korean, Tags.OneBowl, Tags.Rice, Tags.SoupNo, Tags.Hot, Tags.Slightly_Spicy, Tags.Pork),
        food("육회 비빔밥", Tags.Korean, Tags.OneBowl, Tags.Rice, Tags.SoupNo, Tags.Warm, Tags.Slightly_Spicy, Tags.Beef),
        food("제육볶음", Tags.Korean, Tags.Rice, Tags.SoupNo, Tags.Hot, Tags.Spicy, Tags.Pork),
        food("양념갈비", Tags.Korean, Tags.Etc, Tags.SoupNo, Tags.Hot, Tags.Mild, Tags.Pork),
        food("비빔밥", Tags.Korean, Tags.Rice, Tags.OneBowl, Tags.SoupNo, Tags.Warm, Tags.Mild, Tags.Egg),
        food("김밥", Tags.Korean, Tags.Simple, Tags.Rice, Tags.SoupNo, Tags.Warm, Tags.Mild, Tags.Egg, Tags.Pork),
        food("주먹밥", Tags.Korean,Tags.Simple,  Tags.Rice, Tags.SoupNo, Tags.Warm, Tags.Mild, Tags.Seafood),
        food("보쌈", Tags.Korean, Tags.Etc, Tags.SoupNo, Tags.Warm, Tags.Mild, Tags.Pork),
        food("족발", Tags.Korean, Tags.Etc, Tags.SoupNo, Tags.Warm, Tags.Mild, Tags.Pork),
        food("밥버거", Tags.Korean, Tags.Simple, Tags.Rice, Tags.SoupNo, Tags.Warm, Tags.Mild, Tags.Egg, Tags.Pork),
        food("컵밥", Tags.Korean, Tags.Simple, Tags.OneBowl, Tags.Rice, Tags.SoupNo, Tags.Warm, Tags.Mild, Tags.Egg, Tags.Pork),
        food("비빔냉면", Tags.Korean, Tags.Noodle, Tags.SoupNo, Tags.Cold, Tags.Slightly_Spicy, Tags.Pork),
        food("회냉면", Tags.Korean, Tags.Noodle, Tags.SoupNo, Tags.Cold, Tags.Slightly_Spicy, Tags.Fish),
        food("비빔국수", Tags.Korean, Tags.Noodle, Tags.SoupNo, Tags.Cold, Tags.Slightly_Spicy),
        food("회", Tags.Korean, Tags.SoupNo, Tags.Cold, Tags.Mild, Tags.Fish),
        food("양념치킨", Tags.Korean, Tags.SoupNo, Tags.Hot, Tags.Slightly_Spicy, Tags.Chicken),
        food("만두", Tags.Korean, Tags.Simple, Tags.Etc, Tags.SoupNo, Tags.Hot, Tags.Mild, Tags.Pork),
        food("김치만두", Tags.Korean, Tags.Simple, Tags.Etc, Tags.SoupNo, Tags.Hot, Tags.Slightly_Spicy, Tags.Pork),
        food("김치전", Tags.Korean, Tags.Etc, Tags.SoupNo, Tags.Hot, Tags.Slightly_Spicy, Tags.Seafood),
        food("파전", Tags.Korean, Tags.Etc, Tags.SoupNo, Tags.Hot, Tags.Mild, Tags.Vegan),
        food("해물파전", Tags.Korean, Tags.Etc, Tags.SoupNo, Tags.Hot, Tags.Mild, Tags.Seafood),
        food("토스트", Tags.Korean, Tags.Simple, Tags.Bread, Tags.SoupNo, Tags.Warm, Tags.Mild, Tags.Egg, Tags.Pork),
        food("죽",Tags.Korean, Tags.OneBowl, Tags.Rice, Tags.SoupYes, Tags.Hot, Tags.Mild),
        food("참치마요덮밥", Tags.Korean, Tags.OneBowl, Tags.Rice, Tags.SoupNo, Tags.Warm, Tags.Mild, Tags.Fish),
        food("치킨마요덮밥", Tags.Korean, Tags.OneBowl, Tags.Rice, Tags.SoupNo, Tags.Warm, Tags.Mild, Tags.Chicken),
        food("스팸마요덮밥", Tags.Korean, Tags.OneBowl, Tags.Rice, Tags.SoupNo, Tags.Warm, Tags.Mild, Tags.Pork),

        // 일식
        food("초밥", Tags.Japanese, Tags.Rice, Tags.SoupNo, Tags.Cold, Tags.Mild, Tags.Fish),
        food("샤브샤브", Tags.Japanese, Tags.Rice, Tags.SoupYes, Tags.Hot, Tags.Mild, Tags.Beef),
        food("돈까스", Tags.Japanese, Tags.Rice, Tags.SoupNo, Tags.Warm, Tags.Mild, Tags.Pork),
        food("규까츠", Tags.Japanese, Tags.Rice, Tags.SoupNo, Tags.Warm, Tags.Mild, Tags.Beef),
        food("규동", Tags.Japanese, Tags.OneBowl, Tags.Rice, Tags.SoupNo, Tags.Warm, Tags.Mild, Tags.Beef, Tags.Egg),
        food("라멘", Tags.Japanese, Tags.OneBowl, Tags.Noodle, Tags.SoupYes, Tags.Hot, Tags.Mild, Tags.Pork),
        food("가츠동", Tags.Japanese, Tags.OneBowl, Tags.Rice, Tags.SoupNo, Tags.Warm, Tags.Mild, Tags.Pork),
        food("오야코동", Tags.Japanese, Tags.OneBowl, Tags.Rice, Tags.SoupNo, Tags.Warm, Tags.Mild, Tags.Egg, Tags.Chicken),
        food("우동", Tags.Japanese, Tags.OneBowl, Tags.Noodle, Tags.SoupYes, Tags.Hot, Tags.Mild, Tags.FishCake),
        food("사케동(회덮밥)", Tags.Japanese, Tags.OneBowl, Tags.Rice, Tags.SoupNo, Tags.Cold, Tags.Mild, Tags.Fish),
        food("텐동", Tags.Japanese, Tags.OneBowl, Tags.Rice, Tags.SoupNo, Tags.Warm, Tags.Mild, Tags.Fish),
        food("냉모밀", Tags.Japanese, Tags.OneBowl, Tags.Noodle, Tags.SoupYes, Tags.Cold, Tags.Mild),
        food("돈까스덮밥", Tags.Japanese, Tags.OneBowl, Tags.Rice, Tags.SoupNo, Tags.Warm, Tags.Mild, Tags.Pork),
        food("연어덮밥", Tags.Japanese, Tags.OneBowl, Tags.Rice, Tags.SoupNo, Tags.Cold, Tags.Mild, Tags.Fish),
        food("오므라이스", Tags.Japanese, Tags.OneBowl, Tags.Rice, Tags.SoupNo, Tags.Hot, Tags.Mild, Tags.Egg, Tags.Pork),
        food("하이라이스", Tags.Japanese, Tags.OneBowl, Tags.Rice, Tags.SoupNo, Tags.Hot, Tags.Mild, Tags.Beef),

        // 중식
        food("마파두부 덮밥", Tags.Chinese, Tags.OneBowl, Tags.Rice, Tags.SoupYes, Tags.Hot, Tags.Spicy, Tags.Pork, Tags.Beef),
        food("꿔바로우", Tags.Chinese, Tags.SoupNo, Tags.Hot, Tags.Mild, Tags.Pork),
        food("짜장면", Tags.Chinese, Tags.OneBowl, Tags.Noodle, Tags.SoupYes, Tags.Hot, Tags.Mild, Tags.Pork),
        food("짬뽕", Tags.Chinese, Tags.OneBowl, Tags.Noodle, Tags.SoupYes, Tags.Hot, Tags.Spicy, Tags.Seafood),
        food("삼선짬뽕", Tags.Chinese, Tags.OneBowl, Tags.Noodle, Tags.SoupYes, Tags.Hot, Tags.Spicy, Tags.Seafood),
        food("짬뽕밥", Tags.Chinese, Tags.OneBowl, Tags.Rice, Tags.SoupYes, Tags.Hot, Tags.Spicy, Tags.Seafood),
        food("탕수육", Tags.Chinese, Tags.SoupNo, Tags.Hot, Tags.Mild, Tags.Pork),
        food("딤섬", Tags.Chinese, Tags.Etc, Tags.SoupYes, Tags.Hot, Tags.Mild, Tags.Pork, Tags.Seafood),
        food("마라탕", Tags.Chinese, Tags.OneBowl, Tags.Etc, Tags.SoupYes, Tags.Hot, Tags.Spicy, Tags.Pork, Tags.Beef, Tags.Seafood),
        food("마라샹궈", Tags.Chinese, Tags.OneBowl, Tags.Etc, Tags.SoupNo, Tags.Hot, Tags.Spicy, Tags.Pork, Tags.Beef, Tags.Seafood),
        food("양꼬치", Tags.Chinese, Tags.SoupNo, Tags.Hot, Tags.Mild, Tags.Lamb),
        food("양갈비", Tags.Chinese, Tags.SoupNo, Tags.Hot, Tags.Mild, Tags.Lamb),
        food("훠궈", Tags.Chinese, Tags.Etc, Tags.SoupYes, Tags.Hot, Tags.Spicy, Tags.Lamb, Tags.Beef, Tags.Seafood),
        food("볶음밥", Tags.Chinese, Tags.OneBowl, Tags.Rice, Tags.SoupNo, Tags.Hot, Tags.Mild, Tags.Seafood, Tags.Egg, Tags.Pork),
        food("새우 볶음밥", Tags.Chinese, Tags.OneBowl, Tags.Rice, Tags.SoupNo, Tags.Hot, Tags.Mild, Tags.Seafood, Tags.Egg, Tags.Pork),
        food("게살 볶음밥", Tags.Chinese, Tags.OneBowl, Tags.Rice, Tags.SoupNo, Tags.Hot, Tags.Mild, Tags.Seafood, Tags.Egg),
        food("잡채밥", Tags.Chinese, Tags.OneBowl, Tags.Rice, Tags.SoupNo, Tags.Hot, Tags.Mild, Tags.Seafood, Tags.Egg, Tags.Pork),
        food("잡탕밥", Tags.Chinese, Tags.OneBowl, Tags.Rice, Tags.SoupYes, Tags.Hot, Tags.Mild, Tags.Seafood, Tags.Pork),

        // 양식
        food("연어 샐러드", Tags.Western, Tags.Simple, Tags.OneBowl, Tags.SoupNo, Tags.Cold, Tags.Mild, Tags.Fish, Tags.Diet),
        food("샐러드 파스타", Tags.Western, Tags.OneBowl, Tags.Noodle, Tags.SoupNo, Tags.Cold, Tags.Mild, Tags.Diet, Tags.Vegan),
        food("봉골레 파스타", Tags.Western, Tags.OneBowl, Tags.Noodle, Tags.SoupNo, Tags.Warm, Tags.Slightly_Spicy, Tags.Seafood),
        food("로제 파스타", Tags.Western, Tags.OneBowl, Tags.Noodle, Tags.SoupNo, Tags.Warm, Tags.Mild, Tags.Seafood, Tags.Pork),
        food("알리오 올리오", Tags.Western, Tags.OneBowl, Tags.Noodle, Tags.SoupNo, Tags.Warm, Tags.Mild, Tags.Vegan),
        food("비프 스테이크", Tags.Western, Tags.SoupNo, Tags.Hot, Tags.Mild, Tags.Beef),
        food("페퍼로니 피자", Tags.Western, Tags.Bread, Tags.SoupNo, Tags.Hot, Tags.Mild, Tags.Pork),
        food("베이컨 크림 파스타", Tags.Western, Tags.OneBowl, Tags.Noodle, Tags.SoupNo, Tags.Warm, Tags.Mild, Tags.Pork),
        food("까르보나라", Tags.Western, Tags.OneBowl, Tags.Noodle, Tags.SoupNo, Tags.Warm, Tags.Mild, Tags.Egg),
        food("버섯 리조또", Tags.Western, Tags.OneBowl, Tags.Rice, Tags.SoupNo, Tags.Warm, Tags.Mild, Tags.Vegan),
        food("토마토 리조또", Tags.Western, Tags.OneBowl, Tags.Rice, Tags.SoupNo, Tags.Warm, Tags.Mild, Tags.Vegan),
        food("바질 리조또", Tags.Western, Tags.OneBowl, Tags.Rice, Tags.SoupNo, Tags.Warm, Tags.Mild, Tags.Vegan),
        food("트러플 리조또", Tags.Western, Tags.OneBowl, Tags.Rice, Tags.SoupNo, Tags.Warm, Tags.Mild, Tags.Vegan),
        food("치즈 버거", Tags.Western, Tags.Simple, Tags.Bread, Tags.SoupNo, Tags.Warm, Tags.Mild, Tags.Beef),
        food("잠봉뵈르", Tags.Western, Tags.Simple, Tags.Bread, Tags.SoupNo, Tags.Warm, Tags.Mild, Tags.Pork),
        food("파니니", Tags.Western, Tags.Simple, Tags.Bread, Tags.SoupNo, Tags.Warm, Tags.Mild, Tags.Pork),
        food("감바스", Tags.Western, Tags.Bread, Tags.SoupNo, Tags.Hot, Tags.Mild, Tags.Seafood),
        food("피자", Tags.Western, Tags.Bread, Tags.SoupNo, Tags.Hot, Tags.Mild, Tags.Pork, Tags.Beef, Tags.Seafood),
        food("치킨", Tags.Western, Tags.SoupNo, Tags.Hot, Tags.Mild, Tags.Chicken),
        food("시리얼", Tags.Western, Tags.Simple, Tags.Etc, Tags.SoupYes, Tags.Cold, Tags.Mild, Tags.Vegan),
        food("샌드위치", Tags.Western, Tags.Simple, Tags.Bread, Tags.SoupNo, Tags.Cold, Tags.Mild, Tags.Egg, Tags.Pork),
        food("치킨 샌드위치", Tags.Western, Tags.Simple, Tags.Bread, Tags.SoupNo, Tags.Cold, Tags.Mild, Tags.Chicken),

        // 아시안
        food("카레", Tags.Asian, Tags.OneBowl, Tags.Rice, Tags.SoupNo, Tags.Hot, Tags.Mild, Tags.Pork, Tags.Beef, Tags.Chicken),
        food("푸팟퐁 커리", Tags.Asian, Tags.Rice, Tags.SoupNo, Tags.Hot, Tags.Mild, Tags.Seafood),
        food("파인애플 볶음밥", Tags.Asian, Tags.Rice, Tags.SoupNo, Tags.Hot, Tags.Mild, Tags.Seafood),
        food("쌀국수", Tags.Asian, Tags.Simple, Tags.OneBowl,  Tags.Noodle, Tags.SoupYes, Tags.Hot, Tags.Mild, Tags.Beef, Tags.Diet),
        food("분 짜", Tags.Asian, Tags.Noodle, Tags.SoupNo, Tags.Cold, Tags.Mild, Tags.Pork, Tags.Diet),
        food("반미", Tags.Asian, Tags.Simple, Tags.Bread, Tags.SoupNo, Tags.Warm, Tags.Mild, Tags.Pork),
        food("팟타이", Tags.Asian, Tags.Noodle, Tags.SoupNo, Tags.Hot, Tags.Mild, Tags.Seafood, Tags.Chicken),
        food("케밥", Tags.Asian, Tags.Simple, Tags.Bread, Tags.SoupNo, Tags.Hot, Tags.Mild, Tags.Chicken, Tags.Pork, Tags.Beef),
        food("쏨 땀", Tags.Asian, Tags.SoupNo, Tags.Cold, Tags.Slightly_Spicy, Tags.Vegan),
    )
}