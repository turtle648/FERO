<template>
    <div class="p-4 space-y-4">
      <div class="flex items-center justify-between mb-4">
        <TextAtom
          text-size="text-lg"
          font-weight="font-bold"
        >
          Step {{ currentStep }}/3
        </TextAtom>
        <BaseImageAtom
          :src="require(`@/assets/images/icon/sign-up-${currentStep}.png`)"
          :alt="`Step ${currentStep}`"
          class="w-8 h-8"
        />
      </div>
      
      <!-- Step 1: ID Verification -->
      <template v-if="currentStep === 1">
        <div class="space-y-2">
          <LabelAtom htmlFor="signup-id" required>아이디</LabelAtom>
          <div class="flex gap-2">
            <InputAtom
              id="signup-id"
              v-model="form.id"
              type="text"
              placeholder="아이디를 입력하세요"
              :disabled="idVerified"
              required
            />
            <ButtonAtom
              @click="handleVerifyId"
              :disabled="idVerified"
            >
              중복확인
            </ButtonAtom>
          </div>
        </div>
      </template>
  
      <!-- Step 2: Password and User Info -->
      <template v-if="currentStep === 2">
        <div class="space-y-2">
          <LabelAtom htmlFor="signup-password" required>비밀번호</LabelAtom>
          <InputAtom
            id="signup-password"
            v-model="form.password"
            type="password"
            placeholder="비밀번호를 입력하세요"
            required
          />
        </div>
        <div class="space-y-2">
          <LabelAtom htmlFor="signup-password-confirm" required>비밀번호 확인</LabelAtom>
          <InputAtom
            id="signup-password-confirm"
            v-model="form.passwordConfirm"
            type="password"
            placeholder="비밀번호를 다시 입력하세요"
            required
          />
        </div>
      </template>
  
      <!-- Step 3: Character Selection -->
      <template v-if="currentStep === 3">
        <div class="space-y-2">
          <LabelAtom htmlFor="character-selection" required>캐릭터 선택</LabelAtom>
          <!-- 캐릭터 선택 UI 구현 -->
        </div>
      </template>
      
      <ButtonAtom
        @click="handleNext"
        class="w-full mt-4"
      >
        {{ currentStep === 3 ? '완료' : '다음' }}
      </ButtonAtom>
    </div>
  </template>
  
  <script setup>
  import { ref, defineProps, defineEmits } from 'vue'
  import BaseImageAtom from '@/components/atoms/BaseImageAtom.vue'
  import ButtonAtom from '@/components/atoms/ButtonAtom.vue'
  import InputAtom from '@/components/atoms/InputAtom.vue'
  import LabelAtom from '@/components/atoms/LabelAtom.vue'
  import TextAtom from '@/components/atoms/TextAtom.vue'
  
defineProps({
    currentStep: {
      type: Number,
      required: true,
      validator: (value) => value >= 1 && value <= 3
    }
  })
  
  const form = ref({
    id: '',
    idVerified: false,
    password: '',
    passwordConfirm: '',
    character: null
  })
  
  const emit = defineEmits(['next', 'verifyId'])
  
  const handleVerifyId = () => {
    emit('verifyId', form.value.id)
  }
  
  const handleNext = () => {
    emit('next', form.value)
  }
  </script>