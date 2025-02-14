<template>
  <div class="fixed inset-0 flex justify-center items-center bg-black bg-opacity-50 z-[9999]">
    <!-- 튜토리얼 결과 -->
    <div v-if="mode === 'tutorial'" class="bg-white p-6 rounded-lg shadow-lg text-center w-3/4 h-2/3 flex flex-col justify-center">
      <p class="text-lg font-bold mb-4">튜토리얼 완료!</p>
      <button @click="completeFitnessTutorial" class="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600">
        확인
      </button>
    </div>

    <!-- 싱글모드 결과 -->
    <div v-if="mode === 'single'" class="bg-white p-6 rounded-lg shadow-lg text-center w-3/4 h-2/3 flex flex-col justify-center">
      <p class="text-lg font-bold mb-4">싱글모드 결과!</p>
      <p class="text-lg mb-4">Count: {{ count }}</p>
      <hr class="my-4">
      <p>아래의 버튼에 Status Update method 추가 예정</p>
      <button @click="completeFitnessSingle" class="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600">
        확인
      </button>
    </div>

    <!-- 랭크모드 결과 -->
    <div v-if="mode === 'rank'" class="bg-white p-6 rounded-lg shadow-lg text-center w-3/4 h-2/3 flex flex-col justify-center">
      <p class="text-lg font-bold mb-4">랭크모드 결과!</p>
      <p>{{ userStore.accessToken }}</p>
      <p v-if="rankResult">{{ rankResult }}</p>
      <p v-else class="text-red-500">랭크 결과를 불러오지 못했습니다.</p>
      <button @click="completeFitnessRank" class="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600">
        확인
      </button>
    </div>
  </div>
</template>

<script setup>
import { useRouter, useRoute } from "vue-router"
import { useMainStore, TUTORIAL_IDS } from "@/stores/mainStore"
import { onMounted, ref, defineProps, watch } from "vue"
import { useUserStore } from '@/stores/store'
import axios from "axios"

const router = useRouter()
const route = useRoute()
const mainStore = useMainStore()
const userStore = useUserStore()
const mode = ref('')
const rankResult = ref('')

const props = defineProps({
  count: Number,
  result: Object,
})

// result가 변경될 때 로그 출력
watch(() => props.result, (newResult) => {
  if (newResult) {
    console.log("Updated result:", newResult)
    fetchRankResult()
  }
}, { immediate: false })  // 처음 마운트될 때 실행되지 않도록 설정

const completeFitnessTutorial = async () => {
  await mainStore.loadTutorial()
  const tutorialId = Number(route.params.exercise) || null

  if (!tutorialId) {
    console.error("Invalid tutorial ID:", route.params.exercise)
    return
  }

  const tutorial = mainStore.tutorial.find((t) => t.tutorialId === tutorialId)
  if (tutorial) tutorial.completed = true

  mainStore.completeTutorial(TUTORIAL_IDS.SQUAT)
  router.push({ name: 'Main' })
}

const completeFitnessSingle = () => { router.push({ name: 'Main' }) }
const completeFitnessRank = () => { router.push({ name: 'Main' }) }

const fetchRankResult = async () => {
  console.log('프롭데이터:', props)
  console.log('props.result: ', props.result)
  console.log('props.result.roomId: ', props.result.roomId)
  console.log('props.result.peerToken: ', props.result.peerToken)
  console.log('userStore.accessToken: ', userStore.accessToken)

  try {
    const payload = {
      gameId: props.result.roomId,
      opponentToken: props.result.peerToken,
      userToken: userStore.accessToken,
    }
    console.log("Sending Rank Match API Request:", payload)

    const response = await axios.post('https://i12e103.p.ssafy.io:8076/api/v1/matching/endGame', payload)
    
    console.log("Rank Match Response:", response.data)
    rankResult.value = response.data ?? "결과를 불러올 수 없습니다."
  } catch (error) {
    console.error("Error fetching rank match result:", error.response?.data || error)
    rankResult.value = "API 호출 중 오류 발생."
  }
}

onMounted(() => {
  const url = window.location.href
  if (url.includes("tutorial")) {
    mode.value = 'tutorial'
  } else if (url.includes("single-mode")) {
    mode.value = 'single'
  } else if (url.includes("rank-match")) {
    mode.value = 'rank'
  }
})
</script>

<style scoped></style>
