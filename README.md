# CustomCalendar
## 기능 소개
### 메인
메인 화면은 간단한 시계, 왼쪽의 버튼을 누르면 위에서부터 순서대로 시계, 달력, 시간표를 표시합니다.

이 어플리케이션은 약간의 투명도를 가집니다.

크기는 Java Swing이 기본 템플릿으로 디자인이 어렵기 때문에 null Layout으로 디자인하여 크기 조정이 불가하게 하였습니다.
### 달력
처음 시작은 오늘이 들어있는 달의 달력으로 시작합니다.

오른쪽 위 **위 아래 버튼**으로 달을 옮길 수 있습니다.

달력에는 날짜와 그 날에 있는 일정의 갯수가 표시됩니다.

시간표를 작성하셨다면 수업까지 추가됩니다.

날짜를 누르면 일정을 자세히 표시해 줍니다.

일정을 표시하고 있는 칸을 누르면 네이버 지도로 목표 장소를 찾아줍니다.

일정을 자세히 표시하는 중에 오른쪽 위 **+ 버튼** 으로 일정을 추가할 수 있습니다.

또한, **왼쪽 화살표 버튼**으로 달력으로 되돌아갈 수 있습니다.

### 시간표
일월화수목금토 1교시부터 10교시까지 시간표를 작성할 수 있습니다.

오른쪽 위 **+ 버튼** 으로 시간표를 추가할 수 있습니다.

### 데이터 저장
데이터는 json형식으로 같은 폴더에 있는 caldata.json, tabledata.json파일을 이용합니다.

json 파일 수정을 위해 Google Gson을 사용하였습니다.

### 에러
일정이나 시간표를 추가한 직후, 화면이 제대로 표시되지 않을 수 있습니다.

다른 기능으로 옮겨갔다가 되돌아오면 정상적으로 표시됩니다.

원인은 불명입니다.