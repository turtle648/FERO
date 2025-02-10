<!-- views/RankModePage.vue -->
<template>
  <div class="rank-mode-container">
    <h1>{{ exercise }} 대결</h1>
    <div class="time-display">남은 시간: {{ defaultCount }}분</div>
    
    <!-- 상대방 비디오 표시 -->
    <VideoComponent 
      v-if="matchStore.matchedUser"
      :peer-id="matchStore.matchedUser.id"
      :is-initiator="true"
      class="opponent-video"
    />
  </div>
</template>

<script setup>
import { defineProps, ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useMatchStore } from '@/stores/matchStore'
import VideoComponent from '@/components/VideoComponent.vue'

const router = useRouter()
const matchStore = useMatchStore()

const props = defineProps({
  exercise: {
    type: String,
    required: true
  }
})

const defaultCount = ref(2)

onMounted(() => {
  // 매칭되지 않은 상태로 직접 접근 시 매칭 페이지로 리다이렉트
  if (!matchStore.matchedUser) {
    router.push({
      name: 'RankMatch',
      params: { exercise: props.exercise }
    })
  }
})
</script>

<style scoped>
.rank-mode-container {
  @apply min-h-screen bg-gray-100 p-4 relative;
}

.opponent-video {
  @apply fixed top-4 right-4 z-10;
}

.time-display {
  @apply text-xl font-bold mt-4;
}
</style>
