<script setup>
import '@fontsource/press-start-2p'
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import SignInUp from '@/components/modal/SignInUp.vue'

const router = useRouter()
const showModal = ref(false)
const audioPlayer = ref(null)
const audioContext = ref(null) // Web Audio API 컨텍스트
const track = ref(null)

const token = localStorage.getItem('authToken')

// 메인 화면 이동
const goToMain = async () => {
  if (!token) {
    showModal.value = true
  } else {
    try {
      await router.push('/main')
    } catch (error) {
      console.error('라우팅 에러:', error)
    }
  }
}

// 모달 닫기
const closeModal = () => { 
  showModal.value = false 
}

// Web Audio API를 사용한 자동 재생 시도
const playMusic = async () => {
  if (!audioPlayer.value) return

  try {
    // Web Audio API 활성화
    if (!audioContext.value) {
      audioContext.value = new (window.AudioContext || window.webkitAudioContext)()
      track.value = audioContext.value.createMediaElementSource(audioPlayer.value)
      track.value.connect(audioContext.value.destination)
    }

    // 오디오 컨텍스트 활성화
    if (audioContext.value.state === 'suspended') {
      await audioContext.value.resume()
    }

    // 음악 재생
    await audioPlayer.value.play()
    console.log('배경 음악 자동 재생 성공')
  } catch (error) {
    console.log('자동 재생 실패, 브라우저 정책으로 차단됨:', error)
    document.addEventListener('click', handleUserInteraction, { once: true })
  }
}

// 첫 사용자 액션 후 자동 재생 가능하도록 설정
const handleUserInteraction = () => {
  console.log('사용자 상호작용 감지됨! 음악 재생 시작')
  playMusic()
  document.removeEventListener('click', handleUserInteraction)
}

// PWA 실행 시 자동 재생 시도
onMounted(() => {
  playMusic()
})

// PWA 종료 시 음악 정지
onUnmounted(() => {
  document.removeEventListener('click', handleUserInteraction)
  if (audioPlayer.value) {
    audioPlayer.value.pause()
  }
})
</script>

<template>
  <div class="fixed inset-0 flex flex-col justify-center items-center overflow-hidden">
    <!-- 배경 음악 -->
    <audio ref="audioPlayer" loop>
      <source src="@/assets/background_music.mp3" type="audio/mp3">
    </audio>

    <!-- 배경 이미지 -->
    <img 
      src="@/assets/images/background_startpage.png" 
      alt="배경이미지 로딩 에러" 
      class="absolute inset-0 w-full h-full object-cover -z-10"
    >

    <!-- 로고 이미지 -->
    <div class="absolute top-[35%] left-1/2 -translate-x-1/2 -translate-y-1/2 w-full flex justify-center">
      <img 
        src="@/assets/images/logo.png" 
        alt="로고 이미지" 
        class="w-[80%] max-w-4xl"
      >
    </div>

    <!-- 시작 버튼 -->
    <button 
      @click="goToMain"
      class="fixed bottom-[5%] left-1/2 -translate-x-1/2 
        px-8 py-4 whitespace-nowrap
        font-['Press_Start_2P'] text-2xl text-white
        bg-transparent border-none cursor-pointer
        tracking-wider leading-none
        hover:scale-105 active:scale-95
        transition-all duration-200 ease-in-out
        text-shadow-pixel animate-blink
        sm:text-xl sm:px-6 sm:py-3
        xs:text-lg xs:px-4 xs:py-2"
    >
      press here to <span class="text-yellow-300">START</span>
    </button>

    <SignInUp v-if="showModal" @close="closeModal" />
  </div>
</template>



<style scoped>
/* 전체 페이지 스크롤 방지 */
:root {
  overflow: hidden;
  height: 100%;
}

body {
  overflow: hidden;
  height: 100%;
  position: fixed;
  width: 100%;
}

@keyframes blink {
  0%, 100% {
    opacity: 1;
  }
  50% {
    opacity: 0;
  }
}

.animate-blink {
  animation: blink 1.5s infinite;
}

.text-shadow-pixel {
  text-shadow: 
    2px 2px 0 #000,
    -2px -2px 0 #000,
    2px -2px 0 #000,
    -2px 2px 0 #000;
}

/* 반응형 스타일 */
@media (max-width: 768px) {
  .sm\:text-xl {
    font-size: 1.25rem;
    line-height: 1.75rem;
  }
  .sm\:px-6 {
    padding-left: 1.5rem;
    padding-right: 1.5rem;
  }
  .sm\:py-3 {
    padding-top: 0.75rem;
    padding-bottom: 0.75rem;
  }
}

@media (max-width: 480px) {
  .xs\:text-lg {
    font-size: 1.125rem;
    line-height: 1.75rem;
  }
  .xs\:px-4 {
    padding-left: 1rem;
    padding-right: 1rem;
  }
  .xs\:py-2 {
    padding-top: 0.5rem;
    padding-bottom: 0.5rem;
  }
}
</style>`