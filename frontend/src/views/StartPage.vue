<template>
  <div class="start-container">
    <audio ref="audioPlayer" loop>
      <source :src="require('@/assets/background_music.mp3')" type="audio/mp3">
    </audio>
    <video autoplay muted loop playsinline class="background-video">
      <source src="@/assets/background.mp4" type="video/mp4">
    </video>
    <button class="start-button" @click="goToMain">Start</button>
    <!-- 토큰이 없으면 모달 띄우기 -->
    <SignInUp v-if="showModal" @close="closeModal" />
  </div>
</template>

<style scoped>
.start-container {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  overflow: hidden;
}

.background-video {
  position: absolute;
  top: 0; 
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  z-index: -1;
}

.start-button {
  position: relative;
  padding: 1.5rem 4rem;
  font-size: 1.5rem;
  border-radius: 2rem;
  border: none;
  background-color: rgba(0, 123, 255, 0.8); /* 약간의 투명도 추가 */
  color: white;
  cursor: pointer;
  transition: all 0.3s ease;
  z-index: 1;
}

/* 태블릿 크기 */
@media (max-width: 768px) {
  .start-button {
    padding: 1.2rem 3rem;
    font-size: 1.3rem;
  }
}

/* 모바일 크기 */
@media (max-width: 480px) {
  .start-button {
    padding: 1rem 2.5rem;
    font-size: 1.1rem;
  }
}
</style>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import SignInUp from '@/components/modal/SignInUp.vue'

const router = useRouter()
const showModal = ref(false)

// 토큰이 존재하는지 확인
// Pinia 상태 관리 사용하여 토큰 유무 확인해도 괜찮음.
const token = localStorage.getItem('authToken')

const goToMain = () => {
  if (!token) {
    // 토큰이 없으면 모달 띄우기
    showModal.value = true
  } else {
    // 토큰이 있으면 메인 페이지로 이동
    router.push('/main')
  }
}

// 모달 창 닫기
const closeModal = () => { showModal.value = false }

const audioPlayer = ref(null)

// 사용자 상호작용 후 음악 재생
const playMusic = () => {
  if (audioPlayer.value) {
    audioPlayer.value.play()
  }
}

onMounted(() => {
  // 페이지 로드 시 자동 재생을 위한 설정
  document.addEventListener('click', playMusic, { once: true })
})
</script>