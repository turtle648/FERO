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
            <div class="information">
                {{ computeMatchTime(match.date, match.time) }} | {{ match.type }} | {{ match.result }} | {{ match.score }}점
            </div>
            <div class="participants">
                {{ match.partners?.length }} 인 게임
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
import { ref, defineEmits, onMounted } from 'vue'

const matchData = ref() 
const emit = defineEmits(['closeRecord', 'openStatus'])

// 언제 매치 했는지 계산 해주는 함수
const computeMatchTime = (date, time) => {
    // 날짜와 시간을 결합하여 하나의 문자열로 만든 뒤, Date 객체로 변환
    const matchDateString = `${date}T${time}`
    const matchDate = new Date(matchDateString) // "YYYY-MM-DDTHH:MM:SS" 형식으로 Date 객체 생성

    const today = new Date() // 현재 시간
    const timeDiff = today - matchDate // 두 날짜의 차이를 밀리초로 구함
    const diffMinutes = Math.floor(timeDiff / (1000 * 60)) // 분 단위로 변환
    const diffHours = Math.floor(timeDiff / (1000 * 60 * 60)) // 시간 단위로 변환
    const diffDays = Math.floor(timeDiff / (1000 * 60 * 60 * 24)) // 일 단위로 변환
    const diffMonths = today.getMonth() - matchDate.getMonth() + 
                        (today.getFullYear() - matchDate.getFullYear()) * 12 // 월 단위로 변환
    const diffYears = today.getFullYear() - matchDate.getFullYear() // 년 단위로 변환

    if (diffMinutes < 60) { return `${diffMinutes}분 전` // 1시간 이내
    } else if (diffHours < 24) { return `${diffHours}시간 전` // 24시간 이내
    } else if (diffDays < 7) { return `${diffDays}일 전` // 일주일 이내
    } else if (diffDays < 10) { return `1주 전` // 7~10일 사이
    } else if (diffDays < 17) { return `2주 전` // 11~17일 사이
    } else if (diffDays < 24) { return `3주 전` // 18~24일 사이
    } else if (diffDays < 28) { return `4주 전` // 25~28일 사이
    } else if (diffDays < 30) { return `1개월 전` // 29일 부터 한 달 전
    } else if (diffMonths < 12) { return `${diffMonths}개월 전` // 1년 이내
    } else { return `${diffYears}년 전` // 그 외 (1년 이상)
    }
}


// 모달 외부 클릭 시 전적창 모달 종료
const closeRecordModal = () => { emit('closeRecord') }

// 상태창으로 돌아가는 함수
const openStatusModal = () => {
    emit('openStatus')
    emit('closeRecord')
}

onMounted(() => {
    matchData.value = getMatchData()
})
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
