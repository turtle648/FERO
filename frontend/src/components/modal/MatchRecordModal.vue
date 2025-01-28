<template>
<div class="modal" @click="closeRecordModal">
    <div class="modal-content" @click.stop @touchstart="handleTouchStart" @touchmove="handleTouchMove" @touchend="handleTouchEnd">
    <!-- 모달 종료 버튼 -->
    <button id="close-btn" @click="closeRecordModal">X</button>

    <!-- 요소 1: 상태 -->
    <div class="record-element-title">
        <h2>유저 전적 확인창</h2>
    </div>

    <!-- 요소 2: 상태 -->
    <div class="record-element-data" ref="matchDataContainer">
        <div v-for="(match, index) in matchData" :key="index" class="match-record">
            <div class="information">{{ match.date }} - {{ match.type }} - {{ match.result }} - {{ match.score }}</div>
            <div class="participants">
                {{ match.partners.length }} 인 게임
                <!-- 차후 아이콘 or 적당한 걸로 대체 -->
                <button class="participants-btn">참여자 확인</button>
            </div>
        </div>
    </div>

    <button id="back-btn" @click="openStatusModal">뒤로가기</button>
    </div>
</div>
</template>

<script setup>
import { getMatchData } from '@/api/userAPI' // API import
import { ref, defineEmits } from 'vue'

const matchData = ref(getMatchData())

const emit = defineEmits(['closeRecord', 'openStatus'])

// 모달 외부 클릭 시 전적창 모달 종료
const closeRecordModal = () => { emit('closeRecord') }

// 상태창으로 돌아가는 함수
const openStatusModal = () => {
    emit('openStatus')
    emit('closeRecord')
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
    max-height: 100%; /* 자식 요소가 부모의 높이를 넘지 않도록 제한 */
    overflow-y: auto; /* 세로로 넘칠 경우 스크롤 가능하게 설정 */
    text-align: center;
    display: flex;
    flex-direction: column; /* 요소들을 세로로 나열 */
    overflow: hidden; /* 부모 div 영역을 넘어가는 부분을 숨깁니다. */
}

/* 모달 내부요소 사이즈 지정 */
.record-element-title {
    width: 100%; /* 너비는 100% */
    height: 20%; /* 높이를 20%로 설정 */
    background-color: hsl(0, 0%, 95%, 0.6); /* 차후 삭제 */
    border-radius: 50%; /* 차후 삭제 */
}

.record-element-data {
    width: 100%; /* 너비는 100% */
    height: 73%; /* 높이를 10%로 설정 */
    background-color: hsla(125, 100%, 89%, 0.4); /* 차후 삭제 */    
    overflow-y: auto; /* 세로로 넘칠 경우 스크롤 가능 */
}

.match-record {
    height: 10%;
    border: solid;
    border-color: black;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.participants {
    width: 30%;
    font-size: xx-small;
    overflow: hidden;
}
#back-btn,
#close-btn {
    position: absolute;
    padding: 5px 10px;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    font-size: xx-small;
}

#back-btn {
    bottom: 2%;
    right: 2%;
}

#close-btn {
    top: 2%;
    right: 2%;
}

</style>
