<template>
  <img class="background-image" src="@/assets/images/background_image2.png" alt="배경이미지" />
  <div class="container" v-if="!isDesktop">
    <div class="header">
      <div class="header-item level" @click="openStatusModal">레벨, 경험치 (상태창)</div>
      <div class="header-item experience" @click="openSettingModal">설정</div>
      <div class="header-item experience" @click="openAlarmModal"><img src="@/assets/images/icon/alarm.png" alt="" /></div>
    </div>
    <!-- 상태창 -->
    <StatusModal v-if="showStatusModal" @closeStatus="closeStatusModal" />

    <!-- 전적창 -->
    <MatchRecordModal v-if="showRecordModal" @closeRecord="closeRecordModal" />

    <!-- 설정 -->
    <SettingModal v-if="showSettingModal" @closeSetting="closeSettingModal" @openSetting="openSettingModal" />

    <!-- 알림 모달 -->
    <AlarmModal v-if="showAlarmModal" @closeAlarm="closeAlarmModal" @openAlarm="openAlarmModal" />

    <!-- 운동 모드 선택 버튼들 -->
    <!-- <div class="exercise-options" v-show="showExerciseOptions"> -->
    <!-- <div class="option-item" @click="handleSoloExercise">혼자하기</div> -->
    <!-- <div class="option-item" @click="handleMultiExercise">같이하기</div> -->
    <!-- </div> -->

    <!-- 새로운 모달 추가 -->
    <!-- <AloneModal v-if="showAloneModal" @closeAlone="closeAloneModal" /> -->
    <!-- <WithModal v-if="showWithModal" @closeWith="closeWithModal" /> -->

    <div class="footer">
      <!-- class명 추가해서 쓰기 -->
      <!-- <div class="grid-item" @click="toggleExerciseOptions">운동</div> -->

      <div class="grid-item" @click="openFriendModal">친구</div>
      <div class="grid-item" @click="openCalendarModal">달력</div>
      <div class="grid-item" @click="openFitnessModal">운동</div>
      <div class="grid-item" @click="openRecordModal">전적</div>
      <div class="grid-item">퀘스트</div>
    </div>

    <!-- 친구모달 -->
    <FriendListModal v-if="showFriendModal" @closeFriend="closeFriendModal" @openFriend="openFriendModal" />

    <!-- 달력 모달 -->
    <CalendarModal v-if="showCalendarModal" @closeCalendar="closeCalendarModal" @openCalendar="openCalendarModal" />

    <!-- 운동 모달 -->
    <FitnessModal v-if="showFitnessModal" @closeFitness="closeFitnessModal" @openFitness="openFitnessModal" />

    <!-- 퀘스트 모달 -->
  </div>
  <div v-else class="qr-code">
    <h1>(나중에 큐알 들어갈 자리)</h1>
  </div>
</template>

<script setup>
import { ref } from "vue"

// 상태창 관련 변수 및 함수
import StatusModal from "@/components/modal/StatusModal.vue"
const showStatusModal = ref(false)
const openStatusModal = () => {
  if (!isAnyModalOpen()) {
    showStatusModal.value = true
  }
}
const closeStatusModal = () => {
  showStatusModal.value = false
}

// 전적창 관련 변수 및 함수
import MatchRecordModal from "@/components/modal/MatchRecordModal.vue"
const showRecordModal = ref(false)
const openRecordModal = () => {
  if (!isAnyModalOpen()) {
    showRecordModal.value = true
  }
}
const closeRecordModal = () => {
  showRecordModal.value = false
}

// 설정 모달
import SettingModal from "@/components/modal/SettingModal.vue"

const showSettingModal = ref(false)
const openSettingModal = () => {
  if (!isAnyModalOpen()) {
    showSettingModal.value = true
  }
}
const closeSettingModal = () => {
  showSettingModal.value = false
}

// 친구리스트 모달
import FriendListModal from "@/components/modal/FriendListModal.vue"

const showFriendModal = ref(false)
const openFriendModal = () => {
  if (!isAnyModalOpen()) {
    showFriendModal.value = true
  }
}
const closeFriendModal = () => {
  showFriendModal.value = false
}

// 달력 모달
import CalendarModal from "@/components/modal/CalendarModal.vue"

const showCalendarModal = ref(false)
const openCalendarModal = () => {
  if (!isAnyModalOpen()) {
    showCalendarModal.value = true
  }
}
const closeCalendarModal = () => {
  showCalendarModal.value = false
}

// 운동 모달
import FitnessModal from "@/components/modal/FitnessModal.vue"

const showFitnessModal = ref(false)
const openFitnessModal = () => {
  if (!isAnyModalOpen()) {
    showFitnessModal.value = true
  }
}
const closeFitnessModal = () => {
  showFitnessModal.value = false
}

// // 혼자함께 잠시 보류
// import AloneModal from "@/components/modal/AloneModal.vue"
// import WithModal from "@/components/modal/WithModal.vue"

// // 상태 관리
// const showAloneModal = ref(false)
// const showWithModal = ref(false)
// const showExerciseOptions = ref(false)

// const toggleExerciseOptions = () => {
//   showExerciseOptions.value = !showExerciseOptions.value
// }

// const handleSoloExercise = () => {
//   showExerciseOptions.value = false
//   showAloneModal.value = true
// }

// const handleMultiExercise = () => {
//   showExerciseOptions.value = false
//   showWithModal.value = true
// }

// const closeAloneModal = () => {
//   showAloneModal.value = false
// }

// const closeWithModal = () => {
//   showWithModal.value = false
// }

// 알림 모달
import AlarmModal from "@/components/modal/AlarmModal.vue"

const showAlarmModal = ref(false)
const openAlarmModal = () => {
  if (!isAnyModalOpen()) {
    showAlarmModal.value = true
  }
}
const closeAlarmModal = () => {
  showAlarmModal.value = false
}

// 현재 활성화된 모든 모달 상태를 확인하는 함수
const isAnyModalOpen = () => {
  return showStatusModal.value || showRecordModal.value || showSettingModal.value || showAlarmModal.value || showFriendModal.value || showCalendarModal.value || showFitnessModal.value
}
</script>

<style scoped>
.background-image {
  max-width: 100%;
  height: 100vh; /* 뷰포트 기준 100% */
  /* object-fit: cover; */
  background-size: cover;
  display: block; /* 이미지가 인라인 요소로 처리X */
  margin: 0 auto; /* 가로 기준 가운데 정렬 */
}

.footer {
  position: absolute;
  bottom: 0%;
  display: grid;
  grid-template-columns: repeat(5, 1fr); /* 열 4개로로 */
  z-index: 1;
  gap: 2%;
  background-color: rgb(255, 255, 255);
  height: 7%;
  width: calc(100% - 20px);
}

.grid-item {
  background-color: rgba(88, 104, 255, 0.8);
  text-align: center;
  /* border-radius: 50%; */
}

.header {
  display: flex;
  top: 0;
  left: 0;
  z-index: 2;
  justify-content: space-around;
  width: 100%;
  height: 5vh;
  position: absolute;
}

.header-item {
  background-color: rgb(194, 255, 96);
  flex: 1;
}

.exercise-options {
  position: absolute;
  bottom: 7%; /* footer의 height와 동일 */
  left: 0;
  width: calc(100% - 20px);
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 2%;
  padding: 10px;
  background-color: rgba(255, 255, 255, 0.9);
  z-index: 1;
}

.option-item {
  background-color: rgba(88, 104, 255, 0.8);
  text-align: center;
  padding: 10px;
  cursor: pointer;
}

/* 모바일 */
@media screen and (max-width: 821px) {
  .background-wrapper {
    width: 100%;
    height: 100vh;
  }

  .background-image {
    width: 100%;
    height: 100vh;
    object-fit: cover;
    overflow: hidden;
  }

  .footer {
    grid-template-columns: repeat(5, 1fr);
    gap: 2%;
    width: calc(100% - 20px);
    padding: 10px;
  }
}

/* 추후 수정필요 */
/* 태블릿
@media screen and (min-device-width: 700px) and (max-device-width: 821px) {
  .background-wrapper {
    width: 100%;
    height: 100vh;
  }

  .background-image {
    width: 100%;
    height: 100vh;
    object-fit: cover;
    overflow: hidden;
  }

  .footer {
    grid-template-columns: repeat(4, 1fr);
    gap: 10px;
    width: calc(100% - 20px);
    padding: 10px;
  }
} */
</style>
