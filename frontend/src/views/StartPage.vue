<template>
  <div class="relative flex justify-center items-center min-h-screen overflow-hidden">
    <audio ref="audioPlayer" loop>
      <source :src="require('@/assets/background_music.mp3')" type="audio/mp3">
    </audio>
    <img 
      src="@/assets/images/background_startpage.png" 
      alt="배경이미지 로딩 에러" 
      class="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-full h-auto object-contain -z-10"
    >
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

<script setup>
import '@fontsource/press-start-2p'
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import SignInUp from '@/components/modal/SignInUp.vue'

const router = useRouter()
const showModal = ref(false)
const audioPlayer = ref(null)

const token = localStorage.getItem('authToken')

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

const closeModal = () => { 
  showModal.value = false 
}

const playMusic = () => {
  if (audioPlayer.value) {
    audioPlayer.value.play()
      .catch(error => console.log('음악 재생 실패:', error))
  }
}

onMounted(() => {
  document.addEventListener('click', playMusic, { once: true })
})

onUnmounted(() => {
  document.removeEventListener('click', playMusic)
  if (audioPlayer.value) {
    audioPlayer.value.pause()
  }
})
</script>

<style scoped>
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
</style>
