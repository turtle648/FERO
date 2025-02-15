<template>
  <div class="h-full w-full fixed overflow-hidden">
    <audio ref="audioPlayer" loop>
      <source :src="require('@/assets/musics/background_music.mp3')" type="audio/mp3">
    </audio>
    
    <img 
      src="@/assets/images/background_startpage.png" 
      alt="배경이미지 로딩 에러" 
      class="absolute inset-0 w-full h-full object-cover -z-10"
    >
    
    <div class="absolute top-[35%] left-1/2 -translate-x-1/2 -translate-y-1/2 w-full flex justify-center">
      <img 
        src="@/assets/images/logo.png" 
        alt="로고 이미지" 
        class="w-[80%] max-w-4xl"
      >
    </div>
    
    <button 
      @click="goToMain"
      class="fixed bottom-[5%] left-1/2 -translate-x-1/2 
        px-8 py-4 whitespace-nowrap
        font-['Press_Start_2P'] text-2xl text-white
        bg-transparent border-none cursor-pointer
        tracking-wider leading-none
        hover:scale-105 active:scale-95
        transition-all duration-200 ease-in-out
        [text-shadow:2px_2px_0_#000,_-2px_-2px_0_#000,_2px_-2px_0_#000,_-2px_2px_0_#000]
        animate-[blink_1.5s_infinite]
        sm:text-xl sm:px-6 sm:py-3
        xs:text-lg xs:px-4 xs:py-2"
    >
      press here to <span class="text-yellow-300">START</span>
    </button>
    
    <SignInUpModal v-if="showModal" @close="closeModal" />
  </div>
</template>

<style scoped>
@keyframes blink {
  0%, 100% {
    opacity: 1;
  }
  50% {
    opacity: 0;
  }
}
</style>

<script setup>
import '@fontsource/press-start-2p'
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import SignInUpModal from '@/components/modal/SignInUpModal.vue'

const router = useRouter()
const showModal = ref(false)
const audioPlayer = ref(null)

const token = localStorage.getItem('authToken')

const goToMain = async () => {
  if (!token) {
    showModal.value = true
    await nextTick()
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

const playMusic = async () => {
  await nextTick()
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
