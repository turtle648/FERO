<template>
  <div class="fixed inset-0 flex justify-center items-center z-0">
    <!-- 튜토리얼 결과 -->
    <MiniBaseModal v-if="mode === 'tutorial'" title="Result" @close-modal="completeFitnessTutorial">
      <p class="text-lg font-bold mb-1 mt-4">튜토리얼 완료!</p>
      <!-- <button @click="completeFitnessTutorial" class="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600">확인</button> -->
    </MiniBaseModal>

    <!-- 싱글모드 결과 -->
    <MiniBaseModal
      v-if="mode === 'single'"
      title="Result"
      class="bg-white p-6 rounded-lg shadow-lg text-center w-[30vh] h-[35vh] flex flex-col justify-center items-center"
      @close-modal="
        () => {
          completeFitnessSingle()
          isSingleResultModalVisible = true
        }
      "
    >
      <div class="text-container pb-4">
        <p class="mb-4 text-3xl font-bold text-center">Sigle Mode</p>
        <p class="text-base font-dgm mb-4">횟수: {{ count }}</p>
        <p class="text-base font-dgm">운동 시간: {{ exerciseDuration }}</p>
      </div>

      <!-- <button @click="completeFitnessSingle" class="w-[10vh] nes-btn is-primary font-dgm p-1">확인</button> -->
    </MiniBaseModal>

    <BaseModal title="Result" class="single-result-modal" v-if="isSingleResultModalVisible">
      <div class="grid grid-cols-4 gap-4 text-container text-center text-xl">
        <!-- 제목 -->
        <!-- <div class="col-span-4 text-center mb-4">
          <p class="text-3xl font-bold">Single Mode</p>
        </div> -->

        <!-- 운동 전후 스탯 -->
        <div class="col-span-4">
          <h3 class="text-3xl font-semibold mt-4 mb-0">Stats</h3>
        </div>
        <div class="col-span-4 grid grid-cols-4 gap-4">
          <!-- Header Row -->
          <div class="font-bold">Type</div>
          <div class="font-bold">Before</div>
          <div class="font-bold">-></div>
          <div class="font-bold">After</div>

          <!-- Arms -->
          <div class="bg-[rgba(255,99,132,0.8)]">Arm</div>
          <div>{{ singleResult.beforeStats.armsStats }}</div>
          <div>→</div>
          <div>{{ singleResult.afterStats.armsStats }}</div>

          <!-- Legs -->
          <div class="bg-[rgba(54,162,235,0.8)]">Legs</div>
          <div>{{ singleResult.beforeStats.legsStats }}</div>
          <div>→</div>
          <div>{{ singleResult.afterStats.legsStats }}</div>

          <!-- Chest -->
          <div class="bg-[rgba(255,206,86,0.8)]">Chest</div>
          <div>{{ singleResult.beforeStats.chestStats }}</div>
          <div>→</div>
          <div>{{ singleResult.afterStats.chestStats }}</div>

          <!-- Abs -->
          <div class="bg-[rgba(75,192,192,0.8)]">Abs</div>
          <div>{{ singleResult.beforeStats.absStats }}</div>
          <div>→</div>
          <div>{{ singleResult.afterStats.absStats }}</div>

          <!-- Back -->
          <div class="bg-[rgba(153,102,255,0.8)]">Back</div>
          <div>{{ singleResult.beforeStats.backStats }}</div>
          <div>→</div>
          <div>{{ singleResult.afterStats.backStats }}</div>

          <!-- Stamina -->
          <div class="bg-[rgba(255,140,0,0.8)]">Stam</div>
          <div>{{ singleResult.beforeStats.staminaStats }}</div>
          <div>→</div>
          <div>{{ singleResult.afterStats.staminaStats }}</div>

          <!-- EXP -->
          <div class="bg-[rgba(255,50,211,0.8)]">EXP</div>
          <div>{{ singleResult.beforeUserExperience }}</div>
          <div>→</div>
          <div>{{ singleResult.afterUserExperience }}</div>

          <div class="col-span-4 flex justify-center">
            <button class="nes-btn is-error w-[10vh] mb-[2vh]">EXIT</button>
          </div>
        </div>
      </div>
    </BaseModal>

    <!-- 랭크모드 결과 -->
    <!-- <MediumBaseModal title="Result"> -->
    <div v-if="mode === 'rank'" class="bg-white p-6 rounded-lg shadow-lg text-center w-3/4 h-2/3 flex flex-col justify-center">
      <p class="text-lg mb-4 font-dgm">랭크모드 결과</p>
      <div v-if="isLoading" class="flex justify-center items-center">
        <svg class="animate-spin h-8 w-8 text-blue-500" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
          <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
          <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8H4z"></path>
        </svg>
      </div>

      <div v-else-if="rankResult">
        <h2 v-if="props.result.remainTime == -1" class="text-lg font-bold">승리</h2>
        <h2 v-else class="text-lg font-bold">
          {{ rankResult.body.userScore > rankResult.body.opponentScore ? "승리" : rankResult.body.userScore < rankResult.body.opponentScore ? "패배" : "무승부" }}
        </h2>
        <p>{{ rankResult.body.userId }} vs {{ rankResult.body.opponentId }}</p>
        <p>운동 종류: {{ rankResult.body.exerciseId }}</p>
        <p>운동 개수: {{ props.count }}</p>
        <p>랭크 점수 변화: {{ rankResult.body.beforeRankScore }} → {{ rankResult.body.afterRankScore }} ({{ rankResult.body.afterRankScore - rankResult.body.beforeRankScore }})</p>
        <p>
          레벨 변화: {{ rankResult.body.beforeUserLevel }} →
          {{ rankResult.body.afterUserLevel }}
        </p>
        <p>경험치 변화: {{ rankResult.body.beforeUserExperience }} → {{ rankResult.body.afterUserExperience }} ({{ rankResult.body.afterUserExperience - rankResult.body.beforeUserExperience }})</p>
        <p>근력 변화 ==========</p>
        <ul>
          <li>팔: {{ rankResult.body.beforeStats.armsStats }} → {{ rankResult.body.afterStats.armsStats }} ({{ rankResult.body.beforeStats.armsStats - rankResult.body.afterStats.armsStats }})</li>
          <li>다리: {{ rankResult.body.beforeStats.legsStats }} → {{ rankResult.body.afterStats.legsStats }} ({{ rankResult.body.beforeStats.legsStats - rankResult.body.afterStats.legsStats }})</li>
          <li>
            가슴: {{ rankResult.body.beforeStats.chestStats }} → {{ rankResult.body.afterStats.chestStats }} ({{ rankResult.body.beforeStats.chestStats - rankResult.body.afterStats.chestStats }})
          </li>
          <li>복부: {{ rankResult.body.beforeStats.absStats }} → {{ rankResult.body.afterStats.absStats }} ({{ rankResult.body.beforeStats.absStats - rankResult.body.afterStats.absStats }})</li>
          <li>등: {{ rankResult.body.beforeStats.backStats }} → {{ rankResult.body.afterStats.backStats }} ({{ rankResult.body.beforeStats.backStats - rankResult.body.afterStats.backStats }})</li>
          <li>
            지구력: {{ rankResult.body.beforeStats.staminaStats }} → {{ rankResult.body.afterStats.staminaStats }} ({{
              rankResult.body.beforeStats.staminaStats - rankResult.body.afterStats.staminaStats
            }})
          </li>
        </ul>
      </div>
      <div v-else>
        <p class="text-red-500 font-dgm">💫 결과 계산 중 입니다 💫</p>
        <img class="" src="@/assets/images/pesocom.png" alt="" />
      </div>

      <button v-if="mode === 'rank' && isDisabled" disabled class="px-4 py-2 bg-gray-500 text-white rounded">확인</button>
      <button v-else @click="completeFitnessRank" class="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600">확인</button>
    </div>
    <!-- </MediumBaseModal> -->
  </div>
</template>

<script setup>
import { useRouter, useRoute } from "vue-router"
import { useMainStore, TUTORIAL_IDS } from "@/stores/mainStore"
import { onMounted, ref, defineProps, watch } from "vue"
import { useUserStore } from "@/stores/store"
import axios from "axios"
// import MediumBaseModal from "@/components/modal/MediumBaseModal.vue"
import BaseModal from "@/components/modal/BaseModal.vue"
// import SmallBaseModal from "@/components/modal/SmallBaseModal.vue"
import MiniBaseModal from "@/components/modal/MiniBaseModal.vue"

const router = useRouter()
const route = useRoute()
const mainStore = useMainStore()
const userStore = useUserStore()
const mode = ref("")
const rankResult = ref("")
const isDisabled = ref(true)
const isLoading = ref(false) // 로딩 상태
const singleResult = ref({
  userId: "",
  beforeStats: {
    armsStats: 0,
    legsStats: 0,
    chestStats: 0,
    absStats: 0,
    backStats: 0,
    staminaStats: 0,
  },
  afterStats: {
    armsStats: 0,
    legsStats: 0,
    chestStats: 0,
    absStats: 0,
    backStats: 0,
    staminaStats: 0,
  },
  userScore: 0,
  exerciseId: null,
}) // 싱글모드 결과

const isSingleResultModalVisible = ref(false) // single-result-modal 표시 여부

const exerciseDuration = ref(0) // 운동 시간
const exerciseStatsRatioId = ref(2) // 운동 종류

const props = defineProps(["count", "result"])

// result가 변경될 때 API 호출
watch(
  () => props.result,
  (newResult) => {
    console.log("completemodal watch 실행")
    console.log(props.result)

    if (newResult) {
      console.log("Updated result:", newResult)
      isDisabled.value = false
      fetchRankResult(userStore.accessToken, props.result.peerToken)
    }
  },
  { deep: true, immediate: false }
)

// 튜토리얼 완료
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
  router.push({ name: "Main" })
}

// 싱글모드 결과
const completeFitnessSingle = async () => {
  try {
    const requestData = {
      exerciseCnt: props.count, // 운동 횟수
      exerciseDuration: exerciseDuration.value, // 운동 시간
      exerciseStatsRatioId: exerciseStatsRatioId.value, // 운동 종류 ID
    }

    console.log("싱글모드 데이터 전송:", requestData)

    const token = userStore.accessToken

    const response = await axios.post("https://i12e103.p.ssafy.io:8076/api/v1/exercise/single-mode", requestData, {
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    })

    if (response.status === 200) {
      console.log("싱글모드 결과 전송 성공:", response.data)

      // API 응답 데이터를 상태 변수에 저장
      singleResult.value = response.data

      // 모달 활성화
      isSingleResultModalVisible.value = true
    } else {
      console.error("싱글모드 결과 전송 실패:", response.status, response.data)
    }
  } catch (error) {
    console.error("싱글모드 결과 전송 중 오류 발생:", error.response?.data || error.message)
  }
}

// 랭크 결과 API 호출 (최대 3번 재시도)
const fetchRankResult = async (userToken, opponentToken) => {
  let attempts = 0
  isLoading.value = true // 로딩 시작
  console.log(userToken + ":" + opponentToken)

  while (attempts < 3) {
    try {
      const payload = {
        gameId: props.result.roomId,
        opponentToken: opponentToken,
        userToken: userToken,
      }
      console.log(`Rank Match API Request (Attempt ${attempts + 1}):`, payload)

      const response = await axios.post("https://i12e103.p.ssafy.io:8076/api/v1/matching/endGame", payload)
      console.log("Rank Match Response:", response.data)

      rankResult.value = response.data ?? "결과를 불러올 수 없습니다."
      isLoading.value = false // 로딩 종료
      return
    } catch (error) {
      attempts++
      console.error(`Error fetching rank match result (Attempt ${attempts}):`, error.response?.data || error)

      if (attempts >= 3) {
        rankResult.value = "API 호출 중 오류 발생."
        isLoading.value = false // 로딩 종료
      }
    }
  }
}

onMounted(() => {
  const url = window.location.href
  if (url.includes("tutorial")) {
    mode.value = "tutorial"
  } else if (url.includes("single-mode")) {
    mode.value = "single"
  } else if (url.includes("rank-match")) {
    mode.value = "rank"
    console.log("props의 값" + props.count)
    console.log("props의 값" + props.result)

    // 상대방의 예기치 못한 종료로 인해 remainTime이 -1 임
    if (props.result.remainTime == -1) {
      isDisabled.value = false
      fetchRankResult(props.result.peerToken, userStore.accessToken)
      fetchRankResult(userStore.accessToken, props.result.peerToken)
    }
  }
  const pathSegments = route.path.split("/").filter(Boolean) // URL을 '/' 기준으로 분할하고, 빈 요소(마지막 `/`) 제거
  const timeFromUrl = parseInt(pathSegments[pathSegments.length - 1]) // 인지된 시간
  exerciseDuration.value = timeFromUrl
})
</script>

<style scoped></style>
