<!-- views/StartPage.vue -->
<!-- src/
├── components/
│   ├── atoms/
│   │   ├── AudioAtom.vue
│   │   ├── BaseImageAtom.vue
│   │   ├── ButtonAtom.vue
│   │   ├── TextAtom.vue
│   │   └── HighlightedTextAtom.vue
│   │
│   ├── molecules/
│   │   ├── BackgroundImageMolecule.vue
│   │   ├── BackgroundMusicMolecule.vue
│   │   ├── BaseButtonMolecule.vue
│   │   ├── ButtonTextMolecule.vue
│   │   └── LogoImageMolecule.vue
│   │
│   ├── organisms/
│   │   ├── BackgroundOrganism.vue
│   │   ├── LogoOrganism.vue
│   │   └── StartButtonOrganism.vue
│   │
│   └── layouts/
│       └── StartPageLayout.vue
│
└── views/
    └── StartPage.vue -->
<template>
  <StartPageLayout
    :showModal="showModal"
    @goToMain="goToMain"
    @closeModal="closeModal"
    ref="audioPlayerRef"
  />
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import StartPageLayout from '../components/layouts/StartPageLayout.vue'

const router = useRouter()
const showModal = ref(false)
const audioPlayerRef = ref(null)
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
  if (audioPlayerRef.value) {
    audioPlayerRef.value.playAudio()
  }
}

onMounted(() => {
  document.addEventListener('click', playMusic, { once: true })
})

onUnmounted(() => {
  document.removeEventListener('click', playMusic)
  if (audioPlayerRef.value) {
    audioPlayerRef.value.pauseAudio()
  }
})
</script>
