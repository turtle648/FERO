<template>
  <div class="ui-tutorial-page" @click="nextStep">
    <div class="relative w-screen h-screen bg-black">
      <!-- 좌측 상단: 카운트 -->
      <div class="absolute top-4 left-4 text-lg text-white font-bold p-2">1</div>

      <!-- 우측 상단: 타이머 -->
      <div class="absolute top-4 right-4 text-lg text-white font-bold p-2">00:00</div>

      <!-- 중앙: 제목 -->
      <!-- 컴퓨터 이미지 -->
      <img class="w-2/3 h-auto absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2" src="@/assets/furni_1.png" alt="" />

      <!-- 하단 중앙: 종료 버튼 -->
      <ExitButton class="absolute bottom-4 left-1/2 transform -translate-x-1/2" />

      <!-- 우측 하단: 신고 버튼 -->
      <ReportIssueButton class="absolute bottom-4 right-4" />

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
import { useRouter, useRoute } from "vue-router"
import { useMainStore, TUTORIAL_IDS } from "@/stores/mainStore"
import ExitButton from "@/components/button/ExitButton.vue"
import ReportIssueButton from "@/components/button/ReportIssueButton.vue"

const router = useRouter()
const route = useRoute()
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
      const nextExercise = route.params.exercise || TUTORIAL_IDS.SQUAT // 기본값 설정
      if (!nextExercise) {
        console.error("운동 정보가 누락되었습니다.")
        return
      }
      router.push({
        name: "Tutorial",
        params: { exercise: nextExercise },
      })
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
