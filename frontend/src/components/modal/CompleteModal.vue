<template>
  <div class="fixed inset-0 flex justify-center items-center bg-black bg-opacity-50 z-[9999]">
    <!-- 튜토리얼 결과 -->
    <div v-if="mode === 'tutorial'" class="bg-white p-6 rounded-lg shadow-lg text-center">
      <p class="text-lg font-bold mb-4">튜토리얼 완료!</p>
      <button @click="completeFitnessTutorial" class="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600">확인</button>
    </div>
    <div v-if="mode === 'single'" class="bg-white p-6 rounded-lg shadow-lg text-center">
      <p class="text-lg font-bold mb-4">싱글모드 결과!</p>
      <p class="text-lg mb-4">Count: {{ count }}</p>
      <hr>
      <p>아래의 버튼에Status Update method 추가 예정</p>
      <button @click="completeFitnessSingle" class="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600">확인</button>
    </div>
    <div v-if="mode === 'rank'">랭크모드 결과!</div>
  </div>
</template>

<script setup>
import { useRouter, useRoute } from "vue-router"
import { useMainStore, TUTORIAL_IDS } from "@/stores/mainStore"
import { onMounted, ref, defineProps } from "vue"

const router = useRouter()
const route = useRoute()
const mainStore = useMainStore()
const mode = ref('')

// 부모로부터 count를 props로 받음
defineProps({
  count: Number,
})

const completeFitnessTutorial = async () => {
  // URL에서 튜토리얼 ID 추출
  await mainStore.loadTutorial()
  const tutorialId = route.params.exercise
  console.log("Route params:", route.params)

  if (isNaN(tutorialId)) {
    console.error("Invalid tutorial ID:", route.params.id)
    return
  }

  // 튜토리얼 상태 업데이트
  const tutorial = mainStore.tutorial.find((t) => t.tutorialId === tutorialId)
  if (tutorial) tutorial.completed = true

  mainStore.completeTutorial(TUTORIAL_IDS.SQUAT)
  router.push("/main") // 메인 페이지로 이동
}

const completeFitnessSingle = () => {router.push({ name: 'Main' })}

onMounted(() => {
  if (window.location.href.includes("tutorial")) { mode.value = 'tutorial'}
  else if (window.location.href.includes("single-mode")) {mode.value = 'single'}
  else if (window.location.href.includes("rank-mode")) {mode.value = 'rank'}
  else { return }
})
</script>

<style scoped></style>
