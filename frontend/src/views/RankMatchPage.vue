<!-- views/RankMatchPage.vue -->
<template>
  <div v-if="!isMatched" class="rank-match-container">
      <!-- 매칭 진행 상태 표시 -->
      <div class="matching-status">
          <div class="status-text">매칭을 찾는 중입니다...</div>
          <div class="loading-spinner"></div>
      </div>
  </div>
  <VideoComponent :class="{'hidden': !isMatched}" @set-is-matched="setIsMatched" :exercise="exercise"/>
</template>

<script setup>
import { ref } from 'vue'
import VideoComponent from '@/components/videoroom/VideoComponent.vue';
import { useRoute } from 'vue-router';


const route = useRoute();
const exercise = ref(route.params.exercise); // URL 파라미터를 ref로 저장
const isMatched = ref(false);
const setIsMatched = (value) => {
  isMatched.value = value;
  console.log("❤️ setIsMatched::" + isMatched.value);
  
}

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

.hidden {
  visibility: hidden;
}
</style>
