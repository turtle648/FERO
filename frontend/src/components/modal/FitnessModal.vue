<!-- components/modal/FitnessModal.vue -->
<template>
  <BaseModal title="Select Exercise" @close-modal="$emit('close-modal')">
    <!-- 메인 운동 선택 영역 -->
    <div class="flex flex-col w-full space-y-4">
      <!-- 스쿼트 그룹 -->
      <div class="flex flex-col space-y-2 w-full">
        <button class="w-full px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 active:bg-blue-700" @click="handleSquatClick">스쿼트</button>
        <button class="w-full px-4 py-2 bg-green-500 text-white rounded-lg hover:bg-green-600" @click="toggleTutorialComplete">
          {{ isSquatCompleted ? "튜토리얼 완료됨" : "튜토리얼 완료하기" }}
        </button>
      </div>

      <!-- 준비중인 운동들 -->
      <button class="w-full px-4 py-2 bg-gray-300 text-gray-600 rounded-lg cursor-not-allowed" disabled>
        런지
        <span class="text-sm">준비중</span>
      </button>

      <button class="w-full px-4 py-2 bg-gray-300 text-gray-600 rounded-lg cursor-not-allowed" disabled>
        푸쉬업
        <span class="text-sm">준비중</span>
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
          <button class="mode-button" :class="{ 'opacity-50': !selectedNumber }" @click="selectMode('single')" :disabled="!selectedNumber">
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

          <!-- Multi Mode -->
          <!-- <button 
            class="mode-button"
            @click="selectMode('multi')"
          >
            <p class="text-lg font-medium">Multi Mode</p>
          </button> -->

          <!-- Rank Mode -->
          <button class="mode-button" @click="selectMode('rank')" :disabled="isLoading || !isSquatCompleted">
            <p class="text-lg font-medium">
              {{ isLoading ? "처리중..." : "Rank Mode" }}
            </p>
          </button>
        </div>
      </div>
    </div>
  </BaseModal>
</template>

<script setup>
import { ref, computed, defineEmits } from "vue"
import { useRouter } from "vue-router"
import { useMainStore, TUTORIAL_IDS } from "@/stores/mainStore"
import BaseModal from "./BaseModal.vue"

const router = useRouter()
const mainStore = useMainStore()
defineEmits(["close-modal"])

const showModeModal = ref(false)
const selectedNumber = ref(null)
const isLoading = ref(false)

// computed 속성은 유지
const isUICompleted = computed(() => {
  const uiTutorial = mainStore.tutorial.find((t) => t.tutorialId === TUTORIAL_IDS.UI)
  return uiTutorial?.completed || false
})

const isSquatCompleted = computed(() => {
  const squatTutorial = mainStore.tutorial.find((t) => t.tutorialId === TUTORIAL_IDS.SQUAT)
  return squatTutorial?.completed || false
})

// handleSquatClick 함수 수정 - computed 속성 활용
const handleSquatClick = () => {
  if (!isUICompleted.value) {
    router.push({
      name: "UiTutorial",
      params: { exercise: TUTORIAL_IDS.SQUAT }, // 선택한 운동 값 전달
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

const selectMode = async (mode) => {
  const exerciseId = TUTORIAL_IDS.SQUAT // 스쿼트 ID(2) 사용

  switch (mode) {
    case "single":
      if (!selectedNumber.value) return
      router.push({
        name: "SingleMode",
        params: { exercise: exerciseId, count: selectedNumber.value },
      })
      break

    // case "multi":
    //   router.push({
    //     name: "MultiMode",
    //     params: { exercise: exerciseId },
    //   })
    //   break

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
        showModeModal.value = false
      }
      break
  }

  showModeModal.value = false
}

const toggleTutorialComplete = () => {
  // [옵션] 서버와 연동할 경우 mainStore.completeTutorial() 호출
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
