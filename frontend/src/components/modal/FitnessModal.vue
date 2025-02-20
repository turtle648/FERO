<template>
  <BaseModal class="font-dgm" title="Select Exercise" @close-modal="$emit('close-modal')">
    <p style="font-size: 20vh;">1</p>
    <!-- 메인 운동 선택 영역 -->
    <div class="font-dgm grid grid-rows-4 w-full h-full justify-items-center content-between">
      <!-- 스쿼트 그룹 -->
      <div class="relative flex flex-col space-y-2 w-full">
        <button class="nes-btn is-primary w-full h-[10vh] px-[10vw] py-2 text-left bg-blue-500 rounded-lg hover:bg-blue-600 active:bg-blue-700 text-[4vh]" @click="handleSquatClick">
          SQUAT
          <a
            class="nes-btn border border-black flex items-center justify-center w-[3vh] h-[3vh] text-[3vh] absolute bottom-2 right-2 z-20"
            style="color: black !important"
            href="#"
            @click.prevent="restartTutorial"
          >
            ?
          </a>
        </button>
      </div>

      <!-- 준비중인 운동들 -->
      <button class="nes-btn is-disabled w-full h-[10vh] px-[10vw] py-2 bg-gray-300 text-left text-gray-600 rounded-lg cursor-not-allowed text-[4vh] relative">
        LUNGE
        <a class="nes-btn bg-white border border-black flex items-center justify-center w-[3vh] h-[3vh] text-[3vh] absolute bottom-2 right-2 z-20">?</a>
      </button>

      <button class="nes-btn is-disabled w-full h-[10vh] px-[10vw] py-2 bg-gray-300 text-left text-gray-600 rounded-lg cursor-not-allowed text-[4vh]" disabled>
        PUSH UP
        <a class="nes-btn bg-white border border-black flex items-center justify-center w-[3vh] h-[3vh] text-[3vh] absolute bottom-2 right-2 z-20">?</a>
      </button>

      <button class="nes-btn is-disabled w-full h-[10vh] px-[10vw] py-2 bg-gray-300 text-left text-gray-600 rounded-lg cursor-not-allowed text-[4vh]" disabled>
        PLANK
        <a class="nes-btn bg-white border border-black flex items-center justify-center w-[3vh] h-[3vh] text-[3vh] absolute bottom-2 right-2 z-20">?</a>
      </button>
    </div>

    <!-- 모드 선택 모달 -->
    <div v-if="showModeModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white p-5 rounded-lg shadow-md w-[80%] max-w-md">
        <div class="flex justify-between items-center mb-4">
          <h3 class="text-lg font-semibold">Select Mode</h3>
          <button class="text-gray-500 hover:text-gray-700" @click="showModeModal = false">×</button>
        </div>

        <div class="flex flex-col space-y-4">
          <!-- Single Mode -->
          <button class="mode-button" :class="{ 'opacity-50': !selectedNumber }" @click="confirmMode('single')" :disabled="!selectedNumber">
            <p class="text-lg font-medium mb-3">Single Mode</p>
            <div class="flex space-x-4">
              <button
                v-for="num in [1, 2, 5]"
                :key="num"
                :class="[
                  'w-10 h-10 rounded-full flex items-center justify-center transition-colors',
                  selectedNumber === num ? 'bg-white text-indigo-500 font-bold ring-2 ring-indigo-500' : 'bg-indigo-200 text-indigo-700 hover:bg-indigo-300',
                ]"
                @click.stop="selectNumber(num)"
              >
                {{ num }}
              </button>
            </div>
          </button>

          <!-- Rank Mode -->
          <button class="mode-button" @click="confirmMode('rank')" :disabled="isLoading || !isSquatCompleted">
            <p class="text-lg font-medium">
              {{ isLoading ? "처리중..." : "Rank Mode" }}
            </p>
          </button>
        </div>
      </div>
    </div>

    <!-- 게임 시작 확인 모달 -->
    <div v-if="showConfirmationModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-[60]">
      <div class="bg-white p-6 rounded-lg shadow-md w-[80%] max-w-sm">
        <h3 class="text-xl font-semibold mb-4">{{ confirmationMessage }}</h3>
        <div class="flex justify-end space-x-4">
          <button class="nes-btn" @click="closeConfirmationModal">취소</button>
          <button class="nes-btn is-primary" @click="confirmGameStart">확인</button>
        </div>
      </div>
    </div>
  </BaseModal>
</template>

<script setup>
// 기존 코드 유지
import { ref, computed, defineEmits } from "vue"
import { useRouter } from "vue-router"
import { useMainStore, TUTORIAL_IDS } from "@/stores/mainStore"
import BaseModal from "./BaseModal.vue"

const router = useRouter()
const mainStore = useMainStore()
defineEmits(["close-modal"])

const showModeModal = ref(false)
const showConfirmationModal = ref(false)
const selectedNumber = ref(null)
const selectedMode = ref(null)
const confirmationMessage = ref("")
const isLoading = ref(false)

const isUICompleted = computed(() => {
  const uiTutorial = mainStore.tutorial.find((t) => t.tutorialId === TUTORIAL_IDS.UI)
  return uiTutorial?.completed || false
})

const isSquatCompleted = computed(() => {
  const squatTutorial = mainStore.tutorial.find((t) => t.tutorialId === TUTORIAL_IDS.SQUAT)
  return squatTutorial?.completed || false
})

const handleSquatClick = () => {
  if (!isUICompleted.value) {
    router.push({
      name: "UiTutorial",
      params: { exercise: TUTORIAL_IDS.SQUAT },
    })
    return (showModeModal.value = false)
  } else if (!isSquatCompleted.value) {
    router.push({
      name: "Tutorial",
      params: { exercise: TUTORIAL_IDS.SQUAT },
    })
    return (showModeModal.value = false)
  } else {
    return (showModeModal.value = true)
  }
}

const selectNumber = (num) => {
  selectedNumber.value = selectedNumber.value === num ? null : num
}

//선택된 모드에 따라 확인 모달 표시
const confirmMode = (mode) => {
  selectedMode.value = mode
  if (mode === "single") {
    confirmationMessage.value = `싱글모드 ${selectedNumber.value}분을 시작하시겠습니까?`
  } else if (mode === "rank") {
    confirmationMessage.value = "랭크모드를 시작하시겠습니까?"
  }
  showConfirmationModal.value = true
}

// confirmGameStart 함수 추가 - 게임 시작 처리
const confirmGameStart = () => {
  startMode(selectedMode.value)
}

// startMode 함수 수정 - 실제 라우트 이동 처리
const startMode = async (mode) => {
  const exerciseId = TUTORIAL_IDS.SQUAT

  switch (mode) {
    case "single":
      if (!selectedNumber.value) return
      router.push({
        name: "SingleMode",
        params: { exercise: exerciseId, count: selectedNumber.value },
      })
      break

    case "rank":
      try {
        isLoading.value = true
        if (!isSquatCompleted.value) {
          alert("스쿼트 튜토리얼을 먼저 완료해주세요.")
          return
        }
        router.push({
          name: "RankMatch",
          params: { exercise: exerciseId },
        })
      } finally {
        isLoading.value = false
      }
      break
  }

  closeConfirmationModal()
}

// 확인 모달 닫기
const closeConfirmationModal = () => {
  showConfirmationModal.value = false
}

const restartTutorial = () => {
  router.push({ name: "UiTutorial" }) // UiTutorial로 이동
}
</script>

<style scoped>
.mode-button {
  @apply w-full h-24 px-4 py-3 bg-indigo-500 text-white rounded-lg
  hover:bg-indigo-600 transition-colors duration-200
  flex flex-col items-center justify-center
  disabled:bg-gray-400 disabled:cursor-not-allowed;
}
</style>
