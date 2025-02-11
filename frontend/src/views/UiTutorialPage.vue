<template>
  <div class="ui-tutorial-page" @click="nextStep">
    <div class="relative w-screen h-screen bg-gray-100">
      <!-- 좌측 상단: 카운트 -->
      <div class="absolute top-4 left-4 text-lg font-bold bg-white p-2 rounded shadow">카운트: 0</div>

      <!-- 우측 상단: 타이머 -->
      <div class="absolute top-4 right-4 text-lg font-bold bg-white p-2 rounded shadow">타이머: 30초</div>

      <!-- 중앙: 제목 -->
      <!-- 컴퓨터 이미지 -->
      <img class="w-4/5 absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2" src="@/assets/furni_1.png" alt="" />

      <!-- 하단 중앙: 종료 버튼 -->
      <button class="absolute bottom-4 left-1/2 transform -translate-x-1/2 bg-red-500 text-white px-6 py-2 rounded hover:bg-red-600">종료</button>

      <!-- 우측 하단: 신고 버튼 -->
      <button class="absolute bottom-4 right-4 bg-yellow-500 text-white px-6 py-2 rounded hover:bg-yellow-600">신고</button>

      <!-- 설명 단계별로 표시 -->
      <div v-if="currentStep === 1" class="absolute top-[70px] right-[120px] flex items-center space-x-2">
        <span class="bg-black text-white px-3 py-1 rounded">타이머입니다!</span>
      </div>

      <div v-if="currentStep === 2" class="absolute top-[70px] left-[120px] flex items-center space-x-2">
        <span class="bg-black text-white px-3 py-1 rounded">카운트입니다!</span>
      </div>

      <div v-if="currentStep === 3" class="absolute bottom-[70px] left-[50%] transform -translate-x-[50%] flex flex-col items-center space-y-2">
        <span class="bg-black text-white px-3 py-1 rounded">종료 버튼입니다!</span>
      </div>

      <div v-if="currentStep === 4" class="absolute bottom-[70px] right-[120px] flex items-center space-x-2">
        <span class="bg-black text-white px-3 py-1 rounded">신고 버튼입니다!</span>
      </div>

      <div v-if="currentStep === 5" class="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 flex flex-col items-center space-y-4 z-10">
        <button class="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600" @click="completeUiTutorial">완료!</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue"
import { useRouter } from "vue-router"
import { useMainStore, TUTORIAL_IDS } from "@/stores/mainStore"

const router = useRouter()
const mainStore = useMainStore()

// 현재 단계 상태 관리
const currentStep = ref(0) // 초기값은 0 (설명 없음)

// 다음 단계로 이동하는 함수
const nextStep = () => {
  if (currentStep.value < 5) {
    currentStep.value += 1 // 다음 단계로 이동
  }
}

// 튜토리얼 완료 함수
const completeUiTutorial = async () => {
  try {
    const success = await mainStore.completeTutorial(TUTORIAL_IDS.UI)
    if (success) {
      router.push("/main")
    } else {
      console.error("튜토리얼 완료 처리 실패")
    }
  } catch (error) {
    console.error("튜토리얼 완료 중 오류 발생:", error)
  }
}
</script>

<style scoped>
.ui-tutorial-page {
  @apply flex flex-col items-center justify-center h-screen bg-gray-100;
}
</style>
