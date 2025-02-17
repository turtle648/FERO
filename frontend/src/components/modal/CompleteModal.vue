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
      <div v-if="isLoading" class="flex justify-center items-center">
        <svg class="animate-spin h-8 w-8 text-blue-500" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
          <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
          <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8H4z"></path>
        </svg>
      </div>

      <div v-else-if="rankResult">
        <h2 class="text-lg font-bold">
          {{
            rankResult.body.userScore > rankResult.body.opponentScore ? "승리" :
            rankResult.body.userScore < rankResult.body.opponentScore ? "패배" :
            "무승부"
          }}
        </h2>
        <p>{{ rankResult.body.userId }} vs {{ rankResult.body.opponentId }}</p>
        <p>운동 종류: {{ rankResult.body.exerciseId }}</p>
        <p>운동 개수: {{ props.count }}</p>
        <p>랭크 점수 변화: {{ rankResult.body.beforeRankScore }} → {{ rankResult.body.afterRankScore }} ({{ rankResult.body.afterRankScore - rankResult.body.beforeRankScore }})</p>
        <p>레벨 변화: {{ rankResult.body.beforeUserLevel }} → {{ rankResult.body.afterUserLevel }}</p>
        <p>경험치 변화: {{ rankResult.body.beforeUserExperience }} → {{ rankResult.body.afterUserExperience }} ({{ rankResult.body.afterUserExperience - rankResult.body.beforeUserExperience }})</p>
        <p>근력 변화 ==========</p>
        <ul>
          <li>팔: {{ rankResult.body.beforeStats.armsStats }} → {{ rankResult.body.afterStats.armsStats }} ({{ rankResult.body.beforeStats.armsStats - rankResult.body.afterStats.armsStats }})</li>
          <li>다리: {{ rankResult.body.beforeStats.legsStats }} → {{ rankResult.body.afterStats.legsStats }} ({{ rankResult.body.beforeStats.legsStats - rankResult.body.afterStats.legsStats }})</li>
          <li>가슴: {{ rankResult.body.beforeStats.chestStats }} → {{ rankResult.body.afterStats.chestStats }} ({{ rankResult.body.beforeStats.chestStats - rankResult.body.afterStats.chestStats }})</li>
          <li>복부: {{ rankResult.body.beforeStats.absStats }} → {{ rankResult.body.afterStats.absStats }} ({{ rankResult.body.beforeStats.absStats - rankResult.body.afterStats.absStats }})</li>
          <li>등: {{ rankResult.body.beforeStats.backStats }} → {{ rankResult.body.afterStats.backStats }} ({{ rankResult.body.beforeStats.backStats - rankResult.body.afterStats.backStats }})</li>
          <li>지구력: {{ rankResult.body.beforeStats.staminaStats }} → {{ rankResult.body.afterStats.staminaStats }} ({{ rankResult.body.beforeStats.staminaStats - rankResult.body.afterStats.staminaStats }})</li>
        </ul>
      </div>
      
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
const isLoading = ref(false)  // 로딩 상태

const props = defineProps({
  count: Number,
  result: Object,
})

// result가 변경될 때 API 호출
watch(() => props.result, (newResult) => {
  if (newResult) {
    console.log("Updated result:", newResult)
    fetchRankResult()
  }
}, { immediate: false })

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

// 랭크 결과 API 호출 (최대 3번 재시도)
const fetchRankResult = async () => {
  let attempts = 0
  isLoading.value = true  // 로딩 시작

  while (attempts < 3) {
    try {
      const payload = {
        gameId: props.result.roomId,
        opponentToken: props.result.peerToken,
        userToken: userStore.accessToken,
      }
      console.log(`Rank Match API Request (Attempt ${attempts + 1}):`, payload)

      const response = await axios.post('https://i12e103.p.ssafy.io:8076/api/v1/matching/endGame', payload)
      console.log("Rank Match Response:", response.data)

      rankResult.value = response.data ?? "결과를 불러올 수 없습니다."
      isLoading.value = false  // 로딩 종료
      return
    } catch (error) {
      attempts++
      console.error(`Error fetching rank match result (Attempt ${attempts}):`, error.response?.data || error)

      if (attempts >= 3) {
        rankResult.value = "API 호출 중 오류 발생."
        isLoading.value = false  // 로딩 종료
      }
    }
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