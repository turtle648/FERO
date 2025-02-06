<template>
  <div class="container bg-black flex flex-col items-center justify-between p-4 h-screen w-screen">
    <div class="flex justify-between w-full">
      <div class="counter text-white-common">(갯수)</div>
      <div class="timer text-white-common">{{ formattedTime }}</div>
      <select v-model="selectedTime" @change="resetTimer">
        <option :value="1 * 60 * 1000">1분</option>
        <option :value="2 * 60 * 1000">2분</option>
        <option :value="5 * 60 * 1000">5분</option>
      </select>
      <button class="text-white-common" @click="startTimer">시작</button>
    </div>

    <!-- 본인 화면 -->
    <div class="flex-grow flex items-center justify-center border border-white w-full mt-4">
      <p>본인 화면</p>
    </div>

    <!-- 하단 버튼 -->
    <div class="flex justify-between w-full mt-4">
      <button class="bg-gray-700 text-white px-4 py-2 rounded">종료</button>
      <button class="bg-gray-700 text-white px-4 py-2 rounded">신고</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue"

let intervalId = null // setInterval ID 저장 (타이머 초기화용)

const selectedTime = ref(1 * 60 * 1000) // 기본값: 1분
const timeLeft = ref(selectedTime.value) // 남은 시간 (ms)
const formattedTime = ref(formatTime(timeLeft.value)) // 표시할 시간

// 시간을 포맷팅하는 함수 (분:초 형식)
function formatTime(time) {
  const minutes = Math.floor(time / 60000)
  const seconds = Math.floor((time % 60000) / 1000)
  return `${String(minutes).padStart(2, "0")}:${String(seconds).padStart(2, "0")}`
}

// 타이머 시작 함수
function startTimer() {
  clearInterval(intervalId) // 기존 타이머 초기화
  timeLeft.value = selectedTime.value // 선택된 시간으로 초기화
  formattedTime.value = formatTime(timeLeft.value)

  intervalId = setInterval(() => {
    timeLeft.value -= 1000 // 매초마다 시간 감소
    formattedTime.value = formatTime(timeLeft.value)

    if (timeLeft.value <= 0) {
      clearInterval(intervalId) // 타이머 종료
      formattedTime.value = "00:00"
    }
  }, 1000)
}

// 타이머 리셋 함수 (시간 변경 시 호출)
function resetTimer() {
  clearInterval(intervalId) // 기존 타이머 초기화
  timeLeft.value = selectedTime.value // 선택된 시간으로 초기화
  formattedTime.value = formatTime(timeLeft.value)
}
</script>

<style scoped>
.text-white-common {
  @apply text-white;
}

/* .container {
  height: 100%;
  width: 100%;
} */
</style>
