<template>
    <div 
      class="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50"
      @click="handleBackdropClick"
    >
      <div 
        class="bg-white rounded-lg shadow-xl w-full max-w-md mx-4 overflow-hidden"
        @click.stop
      >
        <!-- Modal Header -->
        <ModalHeaderMolecule
          :title="modalTitle"
          :icon="modalIcon"
          @close="$emit('close')"
        />
  
        <!-- Login Form -->
        <template v-if="!isSignUpMode">
          <LoginFormMolecule
            @submit="handleLogin"
          />
          <div class="px-4 py-2 flex justify-center gap-4 text-sm">
            <TextAtom
              v-for="link in loginLinks"
              :key="link.text"
              class="cursor-pointer hover:text-blue-600"
              @click="handleLinkClick(link.action)"
            >
              {{ link.text }}
            </TextAtom>
          </div>
          <SocialLoginMolecule
            @socialLogin="handleSocialLogin"
          />
        </template>
  
        <!-- Sign Up Form -->
        <template v-else>
          <SignUpStepFormMolecule
            :current-step="signUpStep"
            @next="handleSignUpNext"
            @verifyEmail="handleEmailVerification"
          />
        </template>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, computed, defineEmits, defineProps } from 'vue'
  import ModalHeaderMolecule from '@/components/molecules/ModalHeaderMolecule.vue'
  import LoginFormMolecule from '@/components/molecules/LoginFormMolecule.vue'
  import SocialLoginMolecule from '@/components/molecules/SocialLoginMolecule.vue'
  import SignUpStepFormMolecule from '@/components/molecules/SignUpStepFormMolecule.vue'
  import TextAtom from '@/components/atoms/TextAtom.vue'
  
  const props = defineProps({
    isSignUpMode: {
      type: Boolean,
      default: false
    }
  })
  
  const emit = defineEmits([
    'close',
    'login',
    'socialLogin',
    'signUp',
    'findId',
    'findPassword',
    'verifyEmail'
  ])
  
  // State
  const signUpStep = ref(1)
  
  // Computed
  const modalTitle = computed(() => {
    if (!props.isSignUpMode) return '로그인'
    return `회원가입 (${signUpStep.value}/3)`
  })
  
  const modalIcon = computed(() => {
    if (!props.isSignUpMode) {
      return require('@/assets/images/fitnessHeroFromISAEKAI.png')
    }
    return require(`@/assets/images/icon/sign-up-${signUpStep.value}.png`)
  })
  
  const loginLinks = [
    { text: 'ID 찾기', action: 'findId' },
    { text: 'PW 찾기', action: 'findPassword' },
    { text: '회원가입', action: 'signUp' }
  ]
  
  // Methods
  const handleBackdropClick = (e) => {
    if (e.target === e.currentTarget) {
      emit('close')
    }
  }
  
  const handleLogin = (formData) => {
    emit('login', formData)
  }
  
  const handleSocialLogin = (platform) => {
    emit('socialLogin', platform)
  }
  
  const handleLinkClick = (action) => {
    emit(action)
  }
  
  const handleSignUpNext = (formData) => {
    if (signUpStep.value < 3) {
      signUpStep.value++
    } else {
      emit('signUp', formData)
    }
  }
  
  const handleEmailVerification = (email) => {
    emit('verifyEmail', email)
  }
  </script>