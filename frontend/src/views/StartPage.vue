<!-- StartPage.vue (Page)
├── API
│   ├── accountAPI.js
│   └── userAPI.js
│
├── Components
│   ├── layouts/
│   │   └── StartPageLayout.vue
│   │       ├── organisms/
│   │       │   ├── BackgroundOrganism.vue
│   │       │   │   ├── molecules/
│   │       │   │   │   ├── BackgroundImageMolecule.vue
│   │       │   │   │   │   └── atoms/BaseImageAtom.vue
│   │       │   │   │   └── BackgroundMusicMolecule.vue
│   │       │   │   │       └── atoms/AudioAtom.vue
│   │       │   │   └── assets/musics/background_music.mp3
│   │       │   │
│   │       │   ├── LogoOrganism.vue
│   │       │   │   └── molecules/LogoImageMolecule.vue
│   │       │   │       ├── atoms/BaseImageAtom.vue
│   │       │   │       └── assets/images/logo.png
│   │       │   │
│   │       │   ├── StartButtonOrganism.vue
│   │       │   │   ├── molecules/
│   │       │   │   │   ├── BaseButtonMolecule.vue
│   │       │   │   │   │   └── atoms/ButtonAtom.vue
│   │       │   │   │   └── ButtonTextMolecule.vue
│   │       │   │   │       ├── atoms/TextAtom.vue
│   │       │   │   │       └── atoms/HighlightedTextAtom.vue
│   │       │   │   └── assets/fonts/press-start-2p.css
│   │       │   │
│   │       │   └── SignInUpModalOrganism.vue
│   │       │       ├── molecules/
│   │       │       │   ├── ModalHeaderMolecule.vue
│   │       │       │   ├── LoginFormMolecule.vue
│   │       │       │   ├── SocialLoginMolecule.vue
│   │       │       │   └── SignUpStepFormMolecule.vue
│   │       │       └── atoms/
│   │       │           ├── BaseImageAtom.vue
│   │       │           ├── ButtonAtom.vue
│   │       │           ├── InputAtom.vue
│   │       │           ├── LabelAtom.vue
│   │       │           └── TextAtom.vue
│   │       │
│   │       └── assets/images/background_startpage.png
│   │
│   └── assets/
│       ├── images/
│       │   ├── background_startpage.png
│       │   ├── logo.png
│       │   └── icon/
│       │       ├── kakao-talk-icon.png
│       │       ├── google-icon.png
│       │       ├── facebook-icon.png
│       │       └── sign-up-{1,2,3}.png
│       │
│       └── musics/
│           └── background_music.mp3
│
└── Router/
    └── index.js -->
<template>
  <StartPageLayout
    :showModal="showModal"
    @goToMain="goToMain"
    @closeModal="closeModal"
    @login="handleLogin"
    @socialLogin="handleSocialLogin"
    @signUp="handleSignUp"
    @verifyEmail="handleEmailVerification"
    ref="audioPlayerRef"
  />
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import StartPageLayout from '@/components/layouts/StartPageLayout.vue'
import { login, signUp, verifyEmail } from '@/api/accountAPI'

const router = useRouter()
const showModal = ref(false)
const audioPlayerRef = ref(null)
const token = localStorage.getItem('authToken')

// Methods
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

const handleLogin = async (formData) => {
  try {
    const response = await login(formData)
    if (response.success) {
      localStorage.setItem('authToken', response.token)
      await router.push('/main')
    }
  } catch (error) {
    console.error('로그인 에러:', error)
  }
}

const handleSocialLogin = async (platform) => {
  console.log(`${platform} 로그인 시도`)
  // 소셜 로그인 로직 구현
}

const handleSignUp = async (formData) => {
  try {
    const response = await signUp(formData)
    if (response.success) {
      showModal.value = false
      // 회원가입 성공 처리
    }
  } catch (error) {
    console.error('회원가입 에러:', error)
  }
}

const handleEmailVerification = async (email) => {
  try {
    const response = await verifyEmail(email)
    if (response.success) {
      // 이메일 인증 성공 처리
    }
  } catch (error) {
    console.error('이메일 인증 에러:', error)
  }
}

// Audio handling
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
