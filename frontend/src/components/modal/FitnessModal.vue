<template>
  <div class="fitness-modal">
    <button id="close-btn" @click="closeFitnessModal">X</button>
    <h2>운동 선택</h2>
    <div class="exercise-buttons">
      <div class="exercise-container">
        <button class="exercise-btn active" @click="handleSquatClick">
          스쿼트
        </button>
        <button class="tutorial-btn" @click="toggleTutorialComplete">
          {{ isTutorialComplete ? '튜토리얼 완료됨' : '튜토리얼 완료하기' }}
        </button>
      </div>
      <button class="exercise-btn disabled" disabled>
        런지
        <span class="locked-text">준비중</span>
      </button>
      <button class="exercise-btn disabled" disabled>
        푸쉬업
        <span class="locked-text">준비중</span>
      </button>
    </div>

    <!-- 모드 선택 모달 -->
    <div v-if="showModeModal" class="mode-modal">
      <h3>모드 선택</h3>
      <div class="mode-buttons">
        <button @click="selectMode('single')">Single Mode</button>
        <button @click="selectMode('multi')">Multi Mode</button>
        <button @click="selectMode('rank')">Rank Mode</button>
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

const selectMode = (mode) => {
  console.log(`Selected mode: ${mode}`)
  // 여기에 각 모드별 처리 로직 추가
}

const closeFitnessModal = () => {
  emit('closeFitness')
}
</script>

<style scoped>
.fitness-modal {
  width: 40vh;
  height: 60vh;
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: white;
  border-radius: 15px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
}

.exercise-container {
  display: flex;
  flex-direction: column;
  gap: 10px;
  width: 100%;
}

.tutorial-btn {
  padding: 5px 10px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.tutorial-btn:hover {
  background-color: #45a049;
}

.mode-modal {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: white;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.mode-buttons {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.mode-buttons button {
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  background-color: rgba(88, 104, 255, 0.8);
  color: white;
  cursor: pointer;
}

.mode-buttons button:hover {
  background-color: rgba(88, 104, 255, 1);
}

/* 기존 스타일 유지 */
</style>
