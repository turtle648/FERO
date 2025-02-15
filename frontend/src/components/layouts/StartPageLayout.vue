<!-- @/components/layouts/StartPageLayout.vue -->
<template>
  <div class="h-full w-full fixed overflow-hidden">
    <BackgroundOrganism ref="audioPlayer" />
    <LogoOrganism />
    <StartButtonOrganism @click="$emit('goToMain')" />
    
    <!-- SignInUpModalOrganism 통합 -->
    <SignInUpModalOrganism
      v-if="showModal"
      :isSignUpMode="isSignUpMode"
      @close="handleModalClose"
      @login="handleLogin"
      @socialLogin="handleSocialLogin"
      @signUp="handleSignUp"
      @findId="handleFindId"
      @findPassword="handleFindPassword"
      @verifyEmail="handleEmailVerification"
    />
  </div>
</template>

<script setup>
import { ref, defineProps, defineEmits, defineExpose } from 'vue'
import BackgroundOrganism from '@/components/organisms/BackgroundOrganism.vue'
import LogoOrganism from '@/components/organisms/LogoOrganism.vue'
import StartButtonOrganism from '@/components/organisms/StartButtonOrganism.vue'
import SignInUpModalOrganism from '@/components/organisms/SignInUpModalOrganism.vue'

defineProps({
  showModal: Boolean
})

const emit = defineEmits([
  'goToMain',
  'closeModal',
  'login',
  'socialLogin',
  'signUp',
  'verifyEmail'
])

// State
const isSignUpMode = ref(false)
const audioPlayer = ref(null)

// Methods
const handleModalClose = () => {
  isSignUpMode.value = false
  emit('closeModal')
}

const handleLogin = (formData) => {
  emit('login', formData)
}

const handleSocialLogin = (platform) => {
  emit('socialLogin', platform)
}

const handleSignUp = (formData) => {
  emit('signUp', formData)
}

const handleFindId = () => {
  // ID 찾기 로직
  console.log('Find ID')
}

const handleFindPassword = () => {
  // 비밀번호 찾기 로직
  console.log('Find Password')
}

const handleEmailVerification = (email) => {
  emit('verifyEmail', email)
}

// Audio controls
defineExpose({
  playAudio: () => audioPlayer.value?.play(),
  pauseAudio: () => audioPlayer.value?.pause()
})
</script>
