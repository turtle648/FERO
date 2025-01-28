<!-- 
    추가로 진행해야 하는 것.
        1 Status Background Image(도트)
        2 내부에서 사용할 숫자 밑 글씨 만들어야함. (다른 component에서 사용해도 됨)
        3 어떤 정보를 정확히 표기해서 줄건지, 결정해야 함.

    현재 임시로 더미데이터 사용
        localStorage에 아래의 내용이 있어야 사용 가능함
            authToken	test_TOKEN	
            nickName	SSAFY_NO1	
            level	25	
            rank	[Gold, 5]	
            userId	TestID	
-->
<template>
    <div class="modal" @click="closeStatusModal">
      <div class="modal-content" @click.stop>
        <!-- 모달 종료 버튼 -->
        <button id="close-btn" @click="closeStatusModal">X</button>
        
        <!-- 요소 1: 상태 -->
        <div class="status-element status-1">
          <!-- 차후, png로 대체할 것 -->
          <h2>Status</h2>
        </div>
  
        <!-- 요소 2: 닉네임, 레벨, 랭크 -->
        <div class="status-element status-2">
          닉네임: {{ nickName }} | Lv. {{ level }} | Rank: {{ rank }}
        </div>
  
        <!-- 요소 3: 퀘스트 달성률, 게이지바 -->
        <div class="status-element status-3">
          Quest 달성률: ■ ■ ■ ■ □ (80%)
          <div class="progress-bar"></div>
        </div>
  
        <!-- 요소 4: 그래프 이미지 박스 -->
        <div class="status-element status-4">
          <div class="status-graph"></div>
        </div>
        <!-- 전적 확인 버튼 -->
        <button id="record-btn">전적확인</button>
      </div>
    </div>
  </template>
  
<script setup>
import { defineEmits, onMounted, ref } from 'vue'
import { getStatus } from '@/api/userAPI'; // API import

const nickName = ref('')
const level = ref('')
const rank = ref('')

// 모달이 켜질 때 실행할 함수
const getUserStatusInfo = () => {
    // 1. 백에서 정보 불러오기
    getStatus(localStorage.getItem('userId'), localStorage.getItem('authToken'))
    // 2. localStorage Update
    // 3. 
    nickName.value = localStorage.getItem('nickName')
    level.value = localStorage.getItem('level')
    rank.value = localStorage.getItem('rank')

    alert('User Status 정보 불러왔다이')
}

// 모달이 열릴 때 onMounted 훅 사용
onMounted(() => {
    getUserStatusInfo()  // 모달이 열릴 때 함수 실행
})

const emit = defineEmits(['closeStatus'])

// 모달 외부 클릭 시 상태창 모달 종료
const closeStatusModal = () => { emit('closeStatus') }
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
    background-color: hsl(0, 0%, 95%, 0.6); /* 차후 삭제 */
    border-radius: 50%; /* 차후 삭제 */
}

.status-2 {
    width: 100%; /* 너비는 100% */
    height: 10%; /* 높이를 10%로 설정 */
    background-color: hsla(0, 100%, 94%, 0.6); /* 차후 삭제 */
}

.status-3 {
    width: 100%; /* 너비는 100% */
    height: 10%; /* 높이를 10%로 설정 */
    background-color: hsla(234, 100%, 92%, 0.6); /* 차후 삭제 */
}

.status-4 {
    width: 100%; /* 너비는 100% */
    height: 60%; /* 높이를 10%로 설정 */
    background-color: hsla(125, 100%, 89%, 0.4); /* 차후 삭제 */
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

  