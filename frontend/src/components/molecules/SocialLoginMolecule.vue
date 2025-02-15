<template>
    <div class="p-4 space-y-4">
      <TextAtom
        text-size="text-sm"
        text-color="text-gray-500"
        class="text-center block"
      >
        또는 소셜 계정으로 로그인
      </TextAtom>
      <div class="flex justify-center gap-4">
        <ButtonAtom
          v-for="social in socialButtons"
          :key="social.platform"
          variant="secondary"
          @click="$emit('socialLogin', social.platform)"
          class="p-2"
        >
          <BaseImageAtom
            :src="social.icon"
            :alt="social.platform"
            class="w-6 h-6"
          />
        </ButtonAtom>
      </div>
    </div>
  </template>
  
  <script setup>
  import { defineEmits } from 'vue'
  import BaseImageAtom from '@/components/atoms/BaseImageAtom.vue'
  import ButtonAtom from '@/components/atoms/ButtonAtom.vue'
  import TextAtom from '@/components/atoms/TextAtom.vue'
  
  const socialButtons = [
    {
      platform: 'kakao',
      icon: require('@/assets/images/icon/kakao-talk-icon.png')
    },
    {
      platform: 'google',
      icon: require('@/assets/images/icon/google-icon.png')
    },
    {
      platform: 'facebook',
      icon: require('@/assets/images/icon/facebook-icon.png')
    }
  ]
  
  defineEmits(['socialLogin'])
  </script>