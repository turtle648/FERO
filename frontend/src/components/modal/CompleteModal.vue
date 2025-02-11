<template>
  <div class="fixed inset-0 flex justify-center items-center bg-black bg-opacity-50 z-[9999]">
    <div class="bg-white p-6 rounded-lg shadow-lg text-center">
      <p class="text-lg font-bold mb-4">튜토리얼 완료!</p>
      <button @click="completeFitnessTutorial" class="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600">확인</button>
    </div>
  </div>
</template>

<script setup>
import { useRouter, useRoute } from "vue-router"
import { useMainStore } from "@/stores/mainStore"

const router = useRouter()
const route = useRoute()
const mainStore = useMainStore()

const completeFitnessTutorial = () => {
  // URL에서 튜토리얼 ID 추출 및 숫자로 변환
  const tutorialId = Number(route.params.exercise)
  console.log(tutorialId)
  
  if (isNaN(tutorialId)) {
    console.error("Invalid tutorial ID:", route.params.id)
    return
  }

  // 튜토리얼 상태 업데이트
  const tutorial = mainStore.tutorial.find((t) => t.tutorialId === tutorialId)
  if (tutorial) tutorial.completed = true

  router.push("/main") // 메인 페이지로 이동
}
</script>

<style scoped></style>
