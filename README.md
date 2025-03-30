# App:점메추

- 제작 계기
1. 밖에서 활동하다 팀원들과 항상 점심 메뉴가 항상 고민.
2. 음식 메뉴를 추천해주는 웹사이트나 어플을 보면 메뉴 단 하나만 랜덤 출력.
3. 내가 당일 원하는 다양한 음식 카테고리를 고르면 그에 맞는 음식들이 나열되어 그 나열된 메뉴 중 하나를 고르고 싶었음. 

![Map](https://github.com/user-attachments/assets/bf9e8e9d-bad6-4994-81cd-db26d073422e)

### 실행 화면

![image](https://github.com/user-attachments/assets/8712c6ef-3a92-4fd7-b021-45777600fc16)

처음 화면

![image](https://github.com/user-attachments/assets/e818da19-7c39-457e-9eca-a151fe462825)


카테고리 선택 → 중복 선택 가능

![image](https://github.com/user-attachments/assets/ffd24742-8cfe-4743-97f8-03277cca0e02) 
![image](https://github.com/user-attachments/assets/0183c333-780b-4b85-b2f3-752b51fa6025)

1. 카테고리에 전부 해당되는 음식 / 하나씩 해당되는 음식들을 전부 불러오기
2. 유저 선택에 해당되는 메뉴들를 LazyRow 를 통해 정렬
3. 메뉴 아래에 유저의 선택 카테고리 표기
4. 국가별 메뉴 나열 후 아래에 메뉴 5개 무작위로 추천
   

![image](https://github.com/user-attachments/assets/93ae5813-e53b-46c5-900f-5a4b7f912bb6)

랜덤으로 데이터에서 하나 추출
