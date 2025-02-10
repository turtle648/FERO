<!-- views/RankMatchPage.vue -->
<template>
  <div class="rank-match-container">
      <!-- 매칭 진행 상태 표시 -->
      <div v-if="matchStatus === 'searching'" class="matching-status">
          <div class="status-text">매칭을 찾는 중입니다...</div>
          <div class="loading-spinner"></div>
      </div>

      <!-- 매칭 발견 시 수락/거절 UI -->
      <div v-if="matchStatus === 'found'" class="match-found">
          <h3>매치가 발견되었습니다!</h3>
          <div class="button-group">
              <button 
                  @click="acceptMatch" 
                  class="accept-button"
                  :disabled="isLoading"
              >
                  {{ isLoading ? '처리중...' : '수락' }}
              </button>
              <button 
                  @click="declineMatch" 
                  class="decline-button"
                  :disabled="isLoading"
              >
                  거절
              </button>
          </div>
      </div>

      <!-- 에러 상태 UI -->
      <div v-if="matchStatus === 'error'" class="error-status">
          <div class="error-text">매칭 중 오류가 발생했습니다</div>
          <button @click="retryMatching" class="retry-button">
              다시 시도
          </button>
      </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, defineProps } from 'vue'
import { useRouter } from 'vue-router'
import { useMatchStore } from '@/stores/matchStore'
import { storeToRefs } from 'pinia'

// props 정의
const props = defineProps({
  exercise: {
      type: String,
      required: true
  }
})

const router = useRouter()
const matchStore = useMatchStore()
const { matchStatus } = storeToRefs(matchStore)
const isLoading = ref(false)

// 매칭 수락 처리
const acceptMatch = async () => {
  isLoading.value = true
  try {
      await matchStore.acceptMatch()
      router.push({
          name: 'RankMode',
          params: {
              exercise: props.exercise
          }
      })
  } catch (error) {
      console.error('매칭 수락 실패:', error)
      matchStore.matchStatus = 'error'
  } finally {
      isLoading.value = false
  }
}

// 매칭 거절 처리
const declineMatch = async () => {
  isLoading.value = true
  try {
      await matchStore.declineMatch()
  } catch (error) {
      console.error('매칭 거절 실패:', error)
      matchStore.matchStatus = 'error'
  } finally {
      isLoading.value = false
  }
}

// 매칭 재시도
const retryMatching = () => {
  matchStore.initializeWebSocket()
}

onMounted(() => {
  matchStore.initializeWebSocket()
})

onBeforeUnmount(() => {
  if (matchStore.webSocket) {
      matchStore.webSocket.close()
  }
})
</script>

<style scoped>
.rank-match-container {
  @apply flex flex-col items-center justify-center min-h-screen bg-gray-100;
}

.matching-status {
  @apply flex flex-col items-center space-y-4;
}

.status-text {
  @apply text-xl font-semibold text-gray-700;
}

.loading-spinner {
  @apply w-12 h-12 border-4 border-blue-500 border-t-transparent rounded-full animate-spin;
}

.match-found {
  @apply flex flex-col items-center space-y-6 p-6 bg-white rounded-lg shadow-md;
}

.button-group {
  @apply flex space-x-4;
}

.accept-button {
  @apply px-6 py-2 bg-green-500 text-white rounded-lg hover:bg-green-600 
         disabled:bg-gray-400 disabled:cursor-not-allowed;
}

.decline-button {
  @apply px-6 py-2 bg-red-500 text-white rounded-lg hover:bg-red-600
         disabled:bg-gray-400 disabled:cursor-not-allowed;
}

.error-status {
  @apply flex flex-col items-center space-y-4 p-6 bg-white rounded-lg shadow-md;
}

.error-text {
  @apply text-red-500 font-semibold;
}

.retry-button {
  @apply px-6 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600;
}
</style>
