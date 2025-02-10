<template>
  <div class="fitness-modal">
    <button class="close-button" @click="closeFitnessModal">X</button>
    <h2 class="modal-title">운동 선택</h2>
    
    <div class="button-container">
      <div class="exercise-group">
        <button class="primary-button" @click="handleSquatClick">
          스쿼트
        </button>
        <button class="tutorial-button" @click="toggleTutorialComplete">
          {{ isTutorialComplete ? '튜토리얼 완료됨' : '튜토리얼 완료하기' }}
        </button>
      </div>
      
      <button class="disabled-button" disabled>
        런지
        <span class="locked-label">준비중</span>
      </button>
      
      <button class="disabled-button" disabled>
        푸쉬업
        <span class="locked-label">준비중</span>
      </button>
    </div>

    <div v-if="showModeModal" class="mode-modal">
      <div class="mode-header">
        <h3 class="mode-title">모드 선택</h3>
        <button class="close-button" @click="showModeModal = false">X</button>
      </div>
      <div class="mode-button-group">
        <button 
          class="mode-button"
          :class="{ 'mode-button-disabled': !selectedNumber }"
          @click="selectMode('single')"
          :disabled="!selectedNumber"
        >
          <p class="mode-label">Single Mode</p>
          <div class="number-toggle">
            <button 
              v-for="num in [1, 2, 5]" 
              :key="num"
              :class="['number-button', { 'number-active': selectedNumber === num }]"
              @click.stop="selectNumber(num)"
            >
              {{ num }}
            </button>
          </div>
        </button>
        <button class="mode-button" @click="selectMode('multi')">
          <p class="mode-label">Multi Mode</p>
        </button>
        <button class="mode-button" @click="selectMode('rank')">
          <p class="mode-label">Rank Mode</p>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, defineEmits } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const emit = defineEmits(['closeFitness'])

const isTutorialComplete = ref(false)
const showModeModal = ref(false)
const selectedNumber = ref(null)

const toggleTutorialComplete = () => {
  isTutorialComplete.value = !isTutorialComplete.value
}

const handleSquatClick = () => {
  if (!isTutorialComplete.value) {
    router.push('/tutorial')
  } else {
    showModeModal.value = true
  }
}

const selectNumber = (num) => {
  selectedNumber.value = selectedNumber.value === num ? null : num
}

const selectMode = (mode) => {
  const exercise = 'squat' // 현재 선택된 운동
  
  switch(mode) {
    case 'single':
      if (!selectedNumber.value) return
      router.push({
        name: 'SingleMode',
        params: {
          exercise: exercise,
          count: selectedNumber.value
        }
      })
      break
      
    case 'multi':
      router.push({
        name: 'MultiMode',
        params: {
          exercise: exercise
        }
      })
      break
      
    case 'rank':
      router.push({
        name: 'RankMode',
        params: {
          exercise: exercise
        }
      })
      break
  }
  
  showModeModal.value = false
}

const closeFitnessModal = () => {
  emit('closeFitness')
}
</script>

<style scoped>
.fitness-modal {
  @apply fixed top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 
         w-[40vh] h-[60vh] bg-white rounded-xl shadow-lg 
         flex flex-col items-center p-5;
}

.close-button {
  @apply self-end text-gray-500 hover:text-gray-700;
}

.modal-title {
  @apply text-xl font-bold mb-6;
}

.button-container {
  @apply flex flex-col w-full space-y-4;
}

.exercise-group {
  @apply flex flex-col space-y-2 w-full;
}

.primary-button {
  @apply w-full px-4 py-2 bg-blue-500 text-white rounded-lg 
         hover:bg-blue-600 active:bg-blue-700;
}

.tutorial-button {
  @apply w-full px-4 py-2 bg-green-500 text-white rounded-lg 
         hover:bg-green-600;
}

.disabled-button {
  @apply w-full px-4 py-2 bg-gray-300 text-gray-600 rounded-lg 
         cursor-not-allowed;
}

.locked-label {
  @apply text-sm;
}

.mode-modal {
  @apply absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 
         bg-white p-5 rounded-lg shadow-md w-[80%] min-w-[300px];
}

.mode-header {
  @apply flex justify-between items-center mb-4;
}

.mode-title {
  @apply text-lg font-semibold;
}

.mode-button-group {
  @apply flex flex-col space-y-4;
}

.mode-button {
  @apply w-full h-24 px-4 py-3 bg-indigo-500 text-white rounded-lg 
         hover:bg-indigo-600 transition-colors duration-200
         flex flex-col items-center justify-center;
}

.mode-label {
  @apply text-lg font-medium mb-3;
}

.number-toggle {
  @apply flex flex-row space-x-4 mt-2;
}

.number-button {
  @apply w-10 h-10 rounded-full bg-indigo-200 flex items-center justify-center
         hover:bg-indigo-300 transition-colors duration-200 text-indigo-700;
}

.number-active {
  @apply bg-white text-indigo-500 font-bold ring-2 ring-indigo-500;
}
</style>
