<!-- views/RankMatchPage.vue -->
<template>
  <div v-if="!isMatched" class="rank-match-container bg-black">
    <!-- 매칭 진행 상태 표시 -->
    <div class="matching-status">
      <div class="images-container">
        <img src="@/assets/images/pesocom.png" alt="" class="w-[10vh] jump-animation delay-1" />
        <img src="@/assets/images/pesocom.png" alt="" class="w-[10vh] jump-animation delay-2" />
        <img src="@/assets/images/pesocom.png" alt="" class="w-[10vh] jump-animation delay-3" />
      </div>
      <div class="status-text font-dgm text-white">{{ typedText }}</div>
    </div>
    <div class="btn-container">
      <button class="nes-btn is-error" @click="cancelMatching">EXIT</button>
    </div>
  </div>
  <VideoComponent :class="{ hidden: !isMatched }" @set-is-matched="setIsMatched" :exercise="exercise" />
</template>

<script setup>
import { ref, onMounted } from "vue"
import VideoComponent from "@/components/videoroom/VideoComponent.vue"
import { useRoute, useRouter } from "vue-router"

const route = useRoute()
const router = useRouter()
const exercise = ref(route.params.exercise)
const isMatched = ref(false)

const setIsMatched = (value) => {
  isMatched.value = value
  console.log("❤️ setIsMatched::" + isMatched.value)
}

const cancelMatching = () => {
  router.push({ name: "Main" })
}

// 타이핑 효과 관련 로직
const fullText = "매칭을 찾는 중입니다..."
const typedText = ref("")
let typingIndex = 0

const startTypingEffect = () => {
  if (typingIndex < fullText.length) {
    typedText.value += fullText.charAt(typingIndex)
    typingIndex++
    setTimeout(startTypingEffect, 150)
  } else {
    setTimeout(() => {
      typedText.value = ""
      typingIndex = 0
      startTypingEffect()
    }, 1000)
  }
}

onMounted(() => {
  startTypingEffect()
})
</script>

<style scoped>
.rank-match-container {
  @apply flex flex-col items-center justify-center min-h-screen bg-black;
}

.matching-status {
  @apply flex flex-col items-center space-y-4;
}

.images-container {
  @apply flex justify-center items-center space-x-4; /* 가로 정렬 및 간격 추가 */
}

/* 점프 애니메이션 */
@keyframes jump {
  0%,
  100% {
    transform: translateY(0); /* 원래 위치 */
  }
  50% {
    transform: translateY(-20px); /* 위로 이동 */
  }
}

.jump-animation {
  animation: jump 1.5s infinite ease-in-out; /* 점프 애니메이션 */
}

/* 각 이미지에 딜레이 추가 */
.delay-1 {
  animation-delay: 0s;
}
.delay-2 {
  animation-delay: 0.3s;
}
.delay-3 {
  animation-delay: 0.6s;
}

.status-text {
  @apply text-xl font-dgm text-white;
}

.btn-container {
  @apply absolute bottom-8 left-1/2 transform -translate-x-1/2;
}
</style>
