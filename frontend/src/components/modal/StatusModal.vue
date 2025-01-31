<!-- 
    추가로 진행해야 하는 것.
        1 Status Background Image(도트)
        2 내부에서 사용할 숫자 밑 글씨 만들어야함. (다른 component에서 사용해도 됨)
        3 어떤 정보를 정확히 표기해서 줄건지, 결정해야 함.

    현재 더미데이터 사용중
-->
<template>
    <div class="modal" @click="closeStatusModal">
      <div class="modal-content" @click.stop>
        <!-- 모달 종료 버튼 -->
        <button id="close-btn" @click="closeStatusModal">X</button>
        
        <!-- 요소 1: 닉네임, 레벨, 랭크-->
        <div class="status-element status-1">
          <!-- 차후, png로 대체할 것 -->
          <h2>Status</h2>
          <div style="margin-top: 2%;">닉네임: {{ userStatus.nickName }}</div>
          <div style="margin-top: 2%;">
            Lv. {{ userStatus.level }} | Rank: {{ userStatus.rank }}
          </div>
        </div>
        <!-- 요소 2 -->
        <div class="status-element status-2">
            <div>
                전체 승: {{ userStatus.totalWinRate }} | 총 플레이 수: {{ userStatus.totalGames }} | myBest: {{ userStatus.myBestGame }}
            </div>
            <div>
                월간 승: {{ userStatus.monthlyWinRate }} | 월간 게임 수: {{ userStatus.monthlyGames }} | myBest: {{ userStatus.myBestMonthlyGame }}
            </div>
        </div>
  
        <!-- 요소 3: 퀘스트 달성률, 게이지바 -->
        <div class="status-element status-3">
        <div style="margin-bottom: 2%;">
            <span style="margin-right: 10%;">미션 클리어 수: {{ userStatus.questCompletes }}</span>
            <span>월간 클리어 수: {{ userStatus.monthlyQuestCompletes }}</span>
        </div>
        <div class="progress-box">
            일일 달성률:
            <!-- 여기서 박스를 채우기 위해 v-for로 박스를 순회하며 스타일을 적용 -->
            <span 
            v-for="n in 5" 
            :key="n" 
            :class="{
                'filled': (userStatus.todayQuestProgress % 100) / 20 >= n,
                'empty': (userStatus.todayQuestProgress % 100) / 20 < n
            }" 
            class="progress-box-cell">
            </span>
            ({{ userStatus.todayQuestProgress }}%)
        </div>
        </div>
  
        <!-- 요소 4: 그래프 이미지 박스 -->
        <div class="status-element status-4">
          <div class="status-graph">
            <!-- 지울예정 -->
            <div style="color:hsl(0, 0%, 0%, 0.4);"><div>owmoc,20x2 ajdiwankn dwmomalm ;mwa mnxinsp</div><span> kd0wm</span><div>a b   c d    e f   g h   i j k l m n o p q r s t u </div><span> kd0wm</span><div>a    b c d  e   f g h    i j k    l m    n o p q r s </div><span> kd0wm</span><div>owmoc,20x2 ajdiwankn   dwmomalm   ;mwa mnxinsp</div><div>owmoc,20x2 ajdiwankn     dwmomalm ;    mwa   mnxinsp</div><span> kd0wm</span><span> kd0wm</span><div>a  b c  d  e f g  h i  j k l    m    n o  p q r s</div><span> kd0wm</span><div>owmoc,20x2 ajdiwankn   dwmomalm ;    mwa    mnxinsp</div><span> kd0wm</span><div>owmoc,20x2 ajdiwankn   dwmomalm ; mwa  mnxinsp</div><span> kd0wm</span></div>
            .
          </div>
        </div>
        <!-- 전적 확인 버튼 -->
        <button id="record-btn" @click="openRecordModal">전적확인</button>
      </div>
    </div>
  </template>
  
<script setup>
import { defineEmits, onMounted, ref } from 'vue'
import { getStatus } from '@/api/userAPI'; // API import

const userStatus = ref({
    // 기본정보
    'nickName': '', 'level': '', 'rank': '',
    'totalWinRate': '', 'totalGames': '', 'myBestGame': '',
    'monthlyWinRate': '', 'monthlyGames': '', 'myBestMonthlyGame': '',
    'questCompletes': '', 'monthlyQuestCompletes': '', 'todayQuestProgress': '', 
    // 육각으로 표시할 정보
    'stamina': '', 'endurance': '', 'cardio': '', 
    'agility': '', 'strength': '', 'flexibility': '',
})

// 유저 상태 변수에 추가
const getUserStatus = async () => {
    // getStatus()에서 사용자 상태를 가져오는 비동기 작업 수행
    await getStatus()

    for (const key in userStatus.value) {
        if (localStorage.getItem(key)) { userStatus.value[key] = localStorage.getItem(key) }
    }
}


// 모달이 열릴 때 onMounted 훅 사용
onMounted(async () => {
    await getUserStatus()
    console.log('유저 상태창 ==========')
    console.log(userStatus.value)
})


const emit = defineEmits(['closeStatus', 'openRecord'])

// 모달 외부 클릭 시 상태창 모달 종료
const closeStatusModal = () => { emit('closeStatus') }

// 전적버튼 클릭시 상태창 끄기 & 전적창 열기
const openRecordModal = () => { 
    emit('closeStatus')
    emit('openRecord')
}

</script>

<style scoped>

/* 임시 */
h2 {
    margin: 0px;
}

/* 모달 관련 */
.modal {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    display: flex;
    justify-content: center;
    align-items: center;
    background: rgba(0, 0, 0, 0.5); /* 배경에 반투명 검정색 추가 */
}

.modal-content {
    position: absolute;
    background: rgba(255, 255, 255, 0.8); /* 콘텐츠 영역 배경을 반투명 흰색으로 설정 */
    padding: 0;
    border-radius: 10px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
    width: 90%;
    height: 70%;
    text-align: center;
    display: flex;
    flex-direction: column; /* 요소들을 세로로 나열 */
}

/* 모달 내부요소 사이즈 지정 */
.status-1 {
    width: 100%; /* 너비는 100% */
    height: 20%; /* 높이를 20%로 설정 */
    background-color: hsla(125, 100%, 89%, 0.4); /* 차후 삭제 */
    border: solid;
}

.status-2 {
    width: 100%; /* 너비는 100% */
    height: 10%; /* 높이를 10%로 설정 */
    background-color: hsla(125, 100%, 89%, 0.4); /* 차후 삭제 */
    border: solid;
}

.status-3 {
    width: 100%; /* 너비는 100% */
    height: 10%; /* 높이를 10%로 설정 */
    background-color: hsla(125, 100%, 89%, 0.4); /* 차후 삭제 */
    border: solid;
}

.status-4 {
    width: 100%; /* 너비는 100% */
    height: 60%; /* 높이를 10%로 설정 */
    background-color: hsla(125, 100%, 89%, 0.4); /* 차후 삭제 */
    border: solid;
}

.status-graph {
    position: relative;
    width: 100%; /* 부모 요소의 너비에 맞추기 */
    height: 100%; /* 부모 요소의 높이에 맞추기 */
    display: flex; /* Flexbox로 콘텐츠 중앙 정렬 */
    justify-content: center; /* 가로 중앙 정렬 */
    align-items: center; /* 세로 중앙 정렬 */
}

.status-graph::after {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0; /* 요소 안에서 이미지가 꽉 차도록 설정 */
    background-image: url('@/assets/images/status/statusBackgroundImage.png');
    background-size: contain; /* 이미지를 요소 크기에 맞게 꽉 차게 설정 */
    background-repeat: no-repeat; /* 이미지 반복을 막기 */
    background-position: center; /* 이미지의 중심을 기준으로 배치 */
    opacity: 1; /* 투명도 설정 */
    z-index: -1; /* 이미지가 요소 뒤에 위치하도록 설정 */
}

/* progress-box는 박스를 감싸는 div */
.progress-box {
  display: flex;
  width: 100%;  /* 5개의 박스 크기를 맞추기 위한 설정 */
  justify-content: space-between;
}

/* progress-box-cell은 각 박스의 스타일 */
.progress-box-cell {
  width: 10%;
  border: 2px solid #ccc;
  background-color: #fff;
  border-radius: 2px;
}

.filled {
  background-color: #4caf50; /* 채워진 박스 색상 */
}

.empty {
  background-color: #ddd; /* 비어있는 박스 색상 */
}


#record-btn,
#close-btn {
    position: absolute;
    padding: 5px 10px;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    font-size:xx-small;
}

#record-btn {
    bottom: 2%;
    right: 2%;
}

#close-btn {
    top: 2%;
    right: 2%;
}
</style>

  