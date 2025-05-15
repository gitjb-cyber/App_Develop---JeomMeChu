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
