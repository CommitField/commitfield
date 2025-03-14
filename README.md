# 🌱 CommitField_BE
> 커밋필드 (사용자의 깃허브 기록을 활용해 펫 육성 및 업적 달성을 지원하며, 채팅 등 지속적인 동기 부여를 돕는 서비스)

총 개발기간 : `2025.2.18` ~ `2025.03.13` (22일)
개발인원: 5명
  
</br>
<img width="1629" alt="스크린샷 2025-02-13 오후 5 17 27" src="" />

# 🌴 팀원 소개
<table>
  <tr>
    <td><img src="https://avatars.githubusercontent.com/u/15629036?v=4" width="200" height="200"></td>
    <td><img src="https://avatars.githubusercontent.com/u/181931584?v=4" width="200" height="200"></td>
    <td><img src="https://avatars.githubusercontent.com/u/93702730?v=4" width="200" height="200"></td>
    <td><img src="https://avatars.githubusercontent.com/u/82190411?v=4" width="200" height="200"></td>
    <td><img src="https://avatars.githubusercontent.com/u/55117130?v=4" width="200" height="200"></td>
  </tr>
  <tr>
    <td><a href="https://github.com/whale22">김소영</a></td>
    <td><a href="https://github.com/skyeong42">성수경</a></td>
    <td><a href="https://github.com/seoyeonson">손서연</a></td>
    <td><a href="https://github.com/ces0135-hub">백성현</a></td>
    <td><a href="https://github.com/ces0135-hub">윤현승</a></td>
  </tr>
</table>

## 🌴 기술 스택

<img width="1046" alt="스크린샷 2025-03-14 오후 1 18 14" src="https://github.com/user-attachments/assets/48a7e53b-08c6-41bb-8fae-88fc9be322d3" />
</br>

## 🌼 시스템 아키텍쳐

<img width="1545" alt="스크린샷 2025-02-13 오후 1 18 55" src="https://github.com/user-attachments/assets/cc121c13-be01-47fd-a44a-64bf3a6433c7" />
</br>

## 🌳 ERD

<img width="1502" alt="스크린샷 2025-03-14 오후 1 19 59" src="https://github.com/user-attachments/assets/4c50079c-240c-47d3-9c87-e09e13f4d9b5" />
</br>


# 🍀 페이지 별 기능

## 🌺 메인 페이지  

<img width="1600" alt="메인 페이지" src="https://github.com/user-attachments/assets/824e6caa-6a15-4b1b-a747-a5da09d29892" /> 
</br>

### 🍃 기능   
#### 🔹 펫/랭킹
- 계절 별 랭킹 설계로 총 커밋 수와 계절 별 커밋 수.
- 현재 시즌에 맞는 커밋 수에 따라 랭킹을 부여.
#### 🔹 커밋 개수 자동 갱신
- 유저 커밋 수 주기적 갱신.
- 스프링 이벤트를 통한 데이터 일괄 갱신.
#### 🔹 실시간 알림
- 시즌 알림
- 랭킹 알림
- 연속 커밋 축하 알림
- 커밋 부재 알림

## 🌹 펫

<img width="1600" alt="펫" src="https://github.com/user-attachments/assets/766fc5cf-11a4-43ad-bd58-1aa7f8908efa" />  
</br>

### 🍃 기능   
#### 🔹 펫
-  사용자 별 펫 확인 가능.

## 🌻 로그인  

<img width="1600" alt="로그인" src="https://github.com/user-attachments/assets/a3842c23-023c-4eda-965f-4767768cb5cf" />  
</br>

### 🍃 기능  
#### 🔹 로그인 
-  Github API를 이용한 로그인 구현.
-  세션 방식 사용.
  

## 🌷 채팅

<div style="display: flex; justify-content: space-between;">
  <img src="https://github.com/user-attachments/assets/f29670ac-4dbe-4af0-87d5-3e9360a9246e" width="48%" />
  <img src="https://github.com/user-attachments/assets/a09a257f-9ebf-43ec-89f3-7399678e17b2" width="48%" />
</div>


### 🍃 기능  
#### 🔹 채팅 방 관리
- 반장이 채팅 방을 생성 및 삭제 가능.
- 마지막 사용자가 나가면 채팅 방 자동 삭제.
- 채팅 방 목록 조회 (전체, 내가 생성한 방, 참여 중인 방, 좋아요 순, 내가 좋아요한 방).
- 중복 참여 불가.
- 키워드 기반 채팅 방 검색.
- 반장만 채팅 방 이름 수정 가능.
- 비밀번호 설정 가능.

#### 🔹 채팅 기능
- 실시간 양방향 통신 사용자 메시지 전송.
- 채팅 방 내 메시지 조회 (사용자가 들어온 시점부터 확인 가능).

#### 🔹 사용자 관리
- 온라인/오프라인 상태 확인 (로그인, 로그아웃).
- 채팅 방 참여자 목록 조회 가능.
- 채팅 방 좋아요 기능 제공 (좋아요 순 정렬 가능).




